package pbl.week2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pbl.week2.config.exception.ErrorConstant;
import pbl.week2.config.exception.PblException;
import pbl.week2.entity.Board;
import pbl.week2.entity.Love;
import pbl.week2.entity.Member;
import pbl.week2.repository.BoardRepository;
import pbl.week2.repository.LoveRepository;
import pbl.week2.repository.MemberRepository;

import java.util.Optional;

import static pbl.week2.config.exception.ErrorConstant.DEFAULT_ERROR;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoveService {

    private final LoveRepository loveRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void createLove(Long boardId, Long memberId) {
        Board board = getBoardById(boardId);
        Member member = getMemberById(memberId);
        //이미 좋아요를 눌렀을 경우 & 안눌렀을 경우
        loveRepository.isLoveByMember(memberId, boardId)
                .ifPresentOrElse(
                        (love) -> {throw new PblException("이미 좋아요를 누른 게시판 입니다.", DEFAULT_ERROR);} ,         //좋아요가 있을 때
                        () -> loveRepository.save(Love.createLove(member, board))         //좋아요가 없을 때
                );
    }

    @Transactional
    public void deleteLove(Long boardId, Long memberId) {
        Board board = getBoardById(boardId);
        //이미 좋아요를 눌렀을 경우 & 안눌렀을 경우
        loveRepository.isLoveByMember(memberId, boardId)
                .ifPresentOrElse(
                        (love) -> {
                            board.downLove();
                            loveRepository.delete(love);
                        },
                        () -> {throw new PblException("삭제할 좋아요가 없습니다.", DEFAULT_ERROR);}
                );
    }


    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new PblException("존재하지 않는 사용자입니다.", DEFAULT_ERROR));
    }

    private Board getBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new PblException("존재하지 않는 게시판입니다.", DEFAULT_ERROR));
    }


}
