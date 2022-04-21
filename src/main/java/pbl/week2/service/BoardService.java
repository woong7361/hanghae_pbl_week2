package pbl.week2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pbl.week2.config.exception.PblException;
import pbl.week2.entity.Board;
import pbl.week2.entity.Love;
import pbl.week2.entity.Member;
import pbl.week2.entity.entityDto.BoardDto;
import pbl.week2.event.FileDeleteEvent;
import pbl.week2.event.FileRecoverEvent;
import pbl.week2.repository.BoardRepository;
import pbl.week2.repository.LoveRepository;
import pbl.week2.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pbl.week2.config.exception.ErrorConstant.DEFAULT_ERROR;
import static pbl.week2.config.exception.ErrorConstant.FILE_ERROR;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final LoveRepository loveRepository;
    private final FileHandler fileHandler;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * create Board Service
     */
    @Transactional
    public Long createBoard(BoardDto.FileReq createReq, Long userId){
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new PblException("not exist member", DEFAULT_ERROR));

        //파일 경로 추출
        String filePath = fileHandler.getFilePath(createReq.getPicture());
        //db저장
        Board newBoard = boardRepository.save(Board.createBoard(createReq.getContent(), filePath, member));
        //파일 저장 - db와 파일은 시스템이 다르니 수동으로 트랜잭션처리 해주어야 한다.
        try {
            fileHandler.saveFile(createReq.getPicture(), filePath);
        } catch (Exception e) {
            log.info("파일 생성 에러 파일도 롤백(삭제) 시도");
            eventPublisher.publishEvent(new FileDeleteEvent(filePath));   //파일 업로드시 에러가 나면 파일 삭제 시도
            throw new PblException("파일 생성 에러 -> 롤백시도", DEFAULT_ERROR);               //db rollback
        }
        return newBoard.getId();
    }

    public BoardDto.PostResList getBoardList(Long memberId, Pageable pageable) {
        Slice<Board> boards = boardRepository.findAllByOrderByLikeCountDesc(pageable);
        //내가 좋아요 누른 Board 아이디 리스트
        List<Love> memberLoveList = loveRepository.findByMemberIdWithBoard(memberId);
        List<Long> myLikeBoardId = memberLoveList.stream().map(l -> l.getBoard().getId()).collect(Collectors.toList());

        Slice<BoardDto.PostRes> postResList = boards.map(b ->
                new BoardDto.PostRes(
                        b.getMember().getNickname(),
                        b.getContent(),
                        fileHandler.getFileToByte(b.getPicture()),
                        b.getLikeCount(),
                        myLikeBoardId.stream().anyMatch(boardId -> boardId.equals(b.getId())),
                        b.getModifiedAt()
                ));

        return new BoardDto.PostResList(postResList);

    }

    public BoardDto.PostRes getBoard(Long postId, Long memberId) {
        Optional<Board> board = boardRepository.findById(postId);
        List<Love> memberLoveList = memberId != null ? loveRepository.findByMemberIdWithBoard(memberId) : new ArrayList<>();
        Stream<Long> myLikeBoardId = memberLoveList.stream().map(l -> l.getBoard().getId());

        return board.map(b ->
            new BoardDto.PostRes(
                    b.getMember().getNickname(),
                    b.getContent(),
                    fileHandler.getFileToByte(b.getPicture()),
                    b.getLikeCount(),
                    myLikeBoardId.anyMatch(boardId -> boardId.equals(b.getId())),
                    b.getModifiedAt()
            )).orElseThrow(() -> new PblException("존재하지 않는 개시판입니다.", DEFAULT_ERROR));
    }

    @Transactional
    public void patchBoard(Long boardId, Long memberId, BoardDto.FileReq fileReq) {
        //자신의 보드가 맞는지 확인 후 수정
        Board board = boardRepository.findById(boardId).filter(b -> b.getMember().getId().equals(memberId))
                .orElseThrow(() -> new PblException("게시판 주인이 아닙니다.", DEFAULT_ERROR));

        byte[] backupImage = fileHandler.getFileToByte(board.getPicture());
        String backupPath = board.getPicture();

        String filePath = fileHandler.getFilePath(fileReq.getPicture());
        board.patch(fileReq.getContent(), filePath);
        try {
            fileHandler.removeFile(backupPath);
            fileHandler.saveFile(fileReq.getPicture(), filePath);  //자신거 삭제 & 기존 파일 복원
        } catch (Exception e){
            eventPublisher.publishEvent(new FileRecoverEvent(backupPath, backupImage));    //복원
            eventPublisher.publishEvent(new FileDeleteEvent(filePath));                   //삭제
            throw new PblException("파일 저장 에러", FILE_ERROR);
        }
    }

    @Transactional
    public void removeBoard(Long boardId, Long memberId) {
        boardRepository.findById(boardId)
                .filter(board -> board.getMember().getId().equals(memberId))
                .ifPresentOrElse(
                        (board) -> {
                            boardRepository.delete(board);
                            fileHandler.removeFile(board.getPicture());
                        },
                        () -> {throw new PblException("당신의 게시판이 아닙니다.", DEFAULT_ERROR);}
                );
    }

    public Slice<BoardDto.PostRes> getpaging(Pageable pageable) {

        Slice<Board> board = boardRepository.findAllByOrderByLikeCountDesc(pageable);

        Slice<BoardDto.PostRes> map = board.map(b ->
                new BoardDto.PostRes(
                        b.getMember().getNickname(),
                        b.getContent(),
                        fileHandler.getFileToByte(b.getPicture()),
                        b.getLikeCount(),
                        false,
                        b.getModifiedAt()
                ));
        return map;
    }
}
