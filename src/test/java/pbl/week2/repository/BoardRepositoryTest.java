package pbl.week2.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import pbl.week2.config.exception.PblException;
import pbl.week2.entity.Board;
import pbl.week2.entity.Member;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class BoardRepositoryTest {

    @Autowired EntityManager em;
    @Autowired BoardRepository boardRepository;
    @Autowired MemberRepository memberRepository;

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

    private void clearPersistenceContext() {
        em.flush();
        em.clear();
    }

    @Test
    public void createReadBoardTest() throws Exception{
        //given
        Board board = createBoard();
        boardRepository.save(board);
        clearPersistenceContext();
        //when
        Optional<Board> findBoard = boardRepository.findById(board.getId());
        //then
        assertThat(findBoard.map(Board::getContent).orElse(null)).isEqualTo("content1");
    }

    @Test
    public void deleteBoardTest() throws Exception{
        //given
        Board board = createBoard();
        boardRepository.save(board);
        clearPersistenceContext();
        //when
        boardRepository.deleteById(board.getId());
        Optional<Board> findBoard = boardRepository.findById(board.getId());
        //then
        assertThatThrownBy(() -> findBoard.orElseThrow(IllegalArgumentException::new))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("멤버 삭제시 게시판 삭제 테스트 - cascade")
    public void boardDeleteByMemberTest() throws Exception{
        //given
        Member member = createMember();
        Board board = createBoard("content", "picture", member);
        memberRepository.save(member);
        clearPersistenceContext();
        //when
        memberRepository.deleteById(member.getId());
        Optional<Board> findBoard = boardRepository.findById(board.getId());
        //then
        assertThatThrownBy(() -> findBoard.orElseThrow(PblException::new)).isInstanceOf(PblException.class);
    }

    @Test
    public void pagingQueryTest() throws Exception{
        //given
        Member member = createMember();
        for (int i = 0; i < 10; i++) {
            Board board = createBoard("1", "2", member);
            for (int j = 10-i; j > 0; j--) {
                board.upLove();
            }
        }
        memberRepository.save(member);
        clearPersistenceContext();
        //when
        Pageable pageable = PageRequest.of(0, 5);
        Slice<Board> boards = boardRepository.findAllByOrderByLikeCountDesc(pageable);
        //then
        assertThat(boards.getSize()).isEqualTo(5);
        assertThat(boards.stream().map(Board::getLikeCount))
                .isSortedAccordingTo((aLong, t1) -> (int) -(aLong - t1));
    }

}