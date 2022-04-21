package pbl.week2.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pbl.week2.config.exception.PblException;
import pbl.week2.entity.Board;
import pbl.week2.entity.Love;
import pbl.week2.entity.Member;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LoveRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired BoardRepository boardRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired LoveRepository loveRepository;

    private Member createMember() {
        return Member.createMember("user1", "password1", "nickname1");
    }
    private Member createMember(String username, String pw, String nickname) {
        return Member.createMember(username, pw, nickname);
    }
    private Board createBoard() {
        Member member = createMember();
        em.persist(member);
        return Board.createBoard("content1", "picture1", member);
    }
    private Board createBoard(String content, String picture, Member member) {
        return Board.createBoard(content, picture, member);
    }
    private Love createLove() {
        Member member = createMember();
        em.persist(member);
        Board board = createBoard();
        em.persist(board);
        return Love.createLove(member, board);
    }

    private void clearPersistenceContext() {
        em.flush();
        em.clear();
    }

    @Test
    public void createLoveTest() throws Exception{
        //given
        Love love = createLove();
        loveRepository.save(love);
        clearPersistenceContext();
        //when
        Optional<Love> findLove = loveRepository.findById(love.getId());
        //then
        assertThat(findLove.map(Love::getId).orElse(null)).isEqualTo(love.getId());
    }

    @Test
    public void findByMemberIdWithBoardQueryTest() throws Exception{
        //given
        Member member1 = Member.createMember("member1", "123", "nickname1");
        Member member2 = Member.createMember("member2", "123", "nickname1");
        Board board1 = Board.createBoard("content1", "picture1", member1);
        Board board2 = Board.createBoard("content2", "picture2", member1);
        Board board3 = Board.createBoard("content3", "picture3", member2);
        Love love1 = Love.createLove(member1, board1);
        Love love2 = Love.createLove(member2, board1);
        Love love3 = Love.createLove(member1, board2);
        Love love4 = Love.createLove(member1, board3);
        memberRepository.save(member1);
        memberRepository.save(member2);
        clearPersistenceContext();
        //when
        List<Love> loves = loveRepository.findByMemberIdWithBoard(member1.getId());
        //then
        assertThat(loves.size()).isEqualTo(3);
        assertThat(loves.stream().map(Love::getId)).containsExactly(love1.getId(), love3.getId(), love4.getId());
    }

    @Test
    public void isLoveByMemberQuerySuccessTest() throws Exception{
        //given
        Love love = createLove();
        Long memberId = love.getMember().getId();
        Long boardId = love.getBoard().getId();
        loveRepository.save(love);
        clearPersistenceContext();
        //when
        Optional<Love> findLove = loveRepository.isLoveByMember(memberId, boardId);
        //then
        assertThat(findLove.map(Love::getId).orElse(null)).isEqualTo(love.getId());
    }

    @Test
    public void isLoveByMemberQueryFailTest() throws Exception{
        //given
        Love love = createLove();
        Long memberId = love.getMember().getId();
        Long boardId = love.getBoard().getId();
        loveRepository.save(love);

        Board board = createBoard();
        clearPersistenceContext();
        //when
        Optional<Love> findLove = loveRepository.isLoveByMember(memberId, board.getId());
        //then

        assertThatThrownBy(() -> findLove.orElseThrow(PblException::new)).isInstanceOf(PblException.class);
    }


}