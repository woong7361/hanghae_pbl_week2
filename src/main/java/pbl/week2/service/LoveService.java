package pbl.week2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pbl.week2.entity.Board;
import pbl.week2.entity.Love;
import pbl.week2.entity.Member;
import pbl.week2.repository.BoardRepository;
import pbl.week2.repository.LoveRepository;
import pbl.week2.repository.MemberRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoveService {

    private final LoveRepository loveRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public void createLove(Long boardId, Long memberId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("not exist Board"));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("not exist member"));
        //이미 좋아요를 눌렀을 경우 & 안눌렀을 경우
        loveRepository.isLoveByMember(memberId, boardId)
                .ifPresentOrElse(
                        (love) -> { throw new IllegalArgumentException("already like board"); }, //좋아요가 있을 때
                        () -> loveRepository.save(Love.createLove(member, board))                //좋아요가 없을 때
                );
    }

    public void deleteLove(Long boardId, Long memberId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("not exist Board"));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("not exist member"));
        //이미 좋아요를 눌렀을 경우 & 안눌렀을 경우
        loveRepository.isLoveByMember(memberId, boardId)
                .ifPresentOrElse(
                        (love) -> loveRepository.delete(love),                              //좋아요가 있을 때
                        () -> { throw new IllegalArgumentException("not exist like"); }     //좋아요가 없을 때
                );
    }
}
