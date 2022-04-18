package pbl.week2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pbl.week2.entity.Board;
import pbl.week2.entity.Love;
import pbl.week2.entity.Member;
import pbl.week2.entity.entityDto.BoardDto;
import pbl.week2.repository.BoardRepository;
import pbl.week2.repository.LoveRepository;
import pbl.week2.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final LoveRepository loveRepository;

    /**
     * create Board Service
     */
    @Transactional
    public Long createBoard(BoardDto.CreateReq createReq, Long userId) {
        Member member = memberRepository
                .findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("not exist member"));
        Board newBoard = boardRepository.save(Board.createBoard(createReq.getContent(), createReq.getPicture(), member));
        return newBoard.getId();
    }

    public List<BoardDto.PostRes> getBoardList(Long memberId) {
        List<Board> boards = boardRepository.findAll();

        //내가 좋아요 누른 Board 아이디 리스트
        List<Love> memberLoveList = loveRepository.findByMemberId(memberId);
        Stream<Long> myLikeBoardId = memberLoveList.stream().map(l -> l.getBoard().getId());

        List<BoardDto.PostRes> postResList = boards.stream().map(b -> new BoardDto.PostRes(
                b.getContent(),
                b.getPicture(),
                b.getLikeCount(),
                myLikeBoardId.anyMatch(boardId -> boardId.equals(b.getId())),
                b.getModifiedAt()
        )).collect(Collectors.toList());

        return postResList;
    }

    public BoardDto.PostRes getBoard(Long postId, Long memberId) {
        Optional<Board> board = boardRepository.findById(postId);
        List<Love> memberLoveList = memberId != null ? loveRepository.findByMemberId(memberId) : new ArrayList<>();
        Stream<Long> myLikeBoardId = memberLoveList.stream().map(l -> l.getBoard().getId());

        return board.map(b -> new BoardDto.PostRes(
                b.getContent(),
                b.getPicture(),
                b.getLikeCount(),
                myLikeBoardId.anyMatch(boardId -> boardId.equals(b.getId())),
                b.getModifiedAt()
        )).orElseThrow(() -> new IllegalArgumentException("not exist Board"));

    }

    @Transactional
    public void patchBoard(Long boardId, Long memberId, BoardDto.CreateReq patchReq) {
        //자신의 보드가 맞는지 확인 후 수정
       boardRepository.findById(boardId)
                .filter(board -> board.getMember().getId().equals(memberId))
                .ifPresentOrElse(
                        (board) -> board.patch(patchReq),
                        () -> { throw new IllegalArgumentException("not your board"); }
                );
    }

    @Transactional
    public void removeBoard(Long boardId, Long memberId) {
        boardRepository.findById(boardId)
                .filter(board -> board.getMember().getId().equals(memberId))
                .ifPresentOrElse(
                        (board) -> boardRepository.delete(board),
                        () -> { throw new IllegalArgumentException("not your board"); }
                );
    }
}
