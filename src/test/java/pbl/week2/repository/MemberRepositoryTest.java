package pbl.week2.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pbl.week2.entity.Board;
import pbl.week2.entity.Member;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;
    @Autowired
    private BoardRepository boardRepository;


    @Test
    @DisplayName("MemberRepository 저장 확인")
    public void testMember() throws Exception{
        //given
        Member member1 = Member.createMember("member1", "pw1", "nickname1");
        Member member2 = Member.createMember("member2", "pw2", "nickname2");
        memberRepository.save(member1);
        memberRepository.save(member2);
        em.flush();
        em.clear();
        //when
        Optional<Member> findMember = memberRepository.findById(member1.getId());
        //then

        assertThat(findMember.map(Member::getUsername).orElse("empty")).isEqualTo("member1");
    }

    @Test
    @DisplayName("멤버 삭제")
    public void deleteMember() throws Exception{
        Member member1 = Member.createMember("member1", "pw1", "nickname1");
        memberRepository.save(member1);
        Board board1 = Board.createBoard("content1", "picture1", member1);
        Board board2 = Board.createBoard("content2", "picture2", member1);
        boardRepository.save(board1);
        boardRepository.save(board2);
        em.flush();
        em.clear();
        //when
        Optional<Member> findMember = memberRepository.findById(member1.getId());
        memberRepository.delete(findMember.get());
        List<Board> all = boardRepository.findAll();
        //that
        assertThat(all.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("멤버 cascadeTest")
    public void cascadeTest() throws Exception{
        Member member1 = Member.createMember("member1", "pw1", "nickname1");
        Board board1 = Board.createBoard("content1", "picture1", member1);
        Board board2 = Board.createBoard("content2", "picture2", member1);
        memberRepository.save(member1);
        em.flush();
        em.clear();
        //when
        List<Board> all = boardRepository.findAll();
        //that
        assertThat(all.size()).isEqualTo(2);
    }
}