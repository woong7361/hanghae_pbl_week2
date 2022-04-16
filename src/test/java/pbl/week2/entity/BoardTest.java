package pbl.week2.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pbl.week2.repository.BoardRepository;
import pbl.week2.repository.LoveRepository;
import pbl.week2.repository.MemberRepository;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BoardTest {

    @Autowired MemberRepository memberRepository;
    @Autowired BoardRepository boardRepository;
    @Autowired LoveRepository loveRepository;
    @Autowired EntityManager em;


    @Test
    @DisplayName("보드 생성 테스트")
    public void createBoard() throws Exception{
        //given
        Member member1 = Member.createMember("member1", "pw1", "nickname1");
        memberRepository.save(member1);
        Board board1 = Board.createBoard("content1", "picture1", member1);
        Board board2 = Board.createBoard("content2", "picture2", member1);
        boardRepository.save(board1);
        boardRepository.save(board2);
        em.flush();
        em.clear();
        //when
        Optional<Board> findBoard = boardRepository.findById(board1.getId());
        //then
        assertThat(findBoard.map(b -> b.getMember().getNickname()).orElse("empty")).isEqualTo("nickname1");
    }

    @Test
    @DisplayName("보드 삭제 cascade 테스트")
    public void deleteBoard() throws Exception{
        //given
        Member member1 = Member.createMember("member1", "pw1", "nickname1");
        memberRepository.save(member1);
        Board board1 = Board.createBoard("content1", "picture1", member1);
        Board board2 = Board.createBoard("content2", "picture2", member1);
        boardRepository.save(board1);
        boardRepository.save(board2);
        Love.createLove(member1, board1);
        em.flush();
        em.clear();

        //when
        boardRepository.deleteById(board1.getId());
        List<Love> all = loveRepository.findAll();

        //that
        assertThat(all.size()).isEqualTo(0);
    }
}