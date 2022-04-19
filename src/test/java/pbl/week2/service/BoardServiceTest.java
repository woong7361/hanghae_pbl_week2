package pbl.week2.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pbl.week2.entity.Board;
import pbl.week2.entity.Member;
import pbl.week2.entity.entityDto.BoardDto;
import pbl.week2.repository.BoardRepository;
import pbl.week2.repository.MemberRepository;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class BoardServiceTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private BoardRepository boardRepository;
    @Autowired private BoardService boardService;
    @Autowired private EntityManager em;

    @Test
    @Transactional
    public void patchBoard() throws Exception{
        //given
        Member member1 = Member.createMember("memebr1", "password", "nickname1");
        Member member2 = Member.createMember("memebr2", "password", "nickname2");
        Board board = Board.createBoard("content1", "picture", member1);
        memberRepository.save(member1);
        memberRepository.save(member2);
        em.flush();
        em.clear();

        //when
//        boardService.patchBoard(board.getId(), member1.getId(), new BoardDto.CreateReq("new picture", "new content"));

        //then

//        Board findBoard = boardRepository.findById(board.getId()).get();

//        assertThat(findBoard.getContent()).isEqualTo("new content");
//        assertThat(findBoard.getPicture()).isEqualTo("new picture");

    }
}