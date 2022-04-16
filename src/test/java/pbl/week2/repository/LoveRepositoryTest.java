package pbl.week2.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pbl.week2.entity.Board;
import pbl.week2.entity.Love;
import pbl.week2.entity.Member;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LoveRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;
    @Autowired BoardRepository boardRepository;
    @Autowired LoveRepository loveRepository;

    @BeforeEach
    public void init() {
        Member member1 = Member.createMember("member1", "pw1", "nickname1");
        Member member2 = Member.createMember("member2", "pw2", "nickname2");
        memberRepository.save(member1);
        memberRepository.save(member2);
        Board board1 = Board.createBoard("content1", "picture1", member1);
        Board board2 = Board.createBoard("content2", "picture2", member1);
        boardRepository.save(board1);
        boardRepository.save(board2);
        Love love1 = Love.createLove(member1, board1);
        Love love2 = Love.createLove(member2, board1);
        Love love3 = Love.createLove(member1, board2);
        loveRepository.save(love1);
        loveRepository.save(love2);
        loveRepository.save(love3);
        em.flush();
        em.clear();
    }

    @Test
    public void createLove() throws Exception{
        //given

        //when
        Love love = em.createQuery("select l from Love l join l.board b on l.board.id = 3 where l.member.id = :memberId", Love.class)
                .setParameter("memberId", 1L)
                .getSingleResult();
        System.out.println("love = " + love);
        //then
    }


}