package pbl.week2.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pbl.week2.config.exception.PblException;
import pbl.week2.entity.Board;
import pbl.week2.entity.Love;
import pbl.week2.entity.Member;
import pbl.week2.repository.BoardRepository;
import pbl.week2.repository.LoveRepository;
import pbl.week2.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LoveServiceTest {

    @Mock LoveRepository loveRepository;
    @Mock MemberRepository memberRepository;
    @Mock BoardRepository boardRepository;

    @InjectMocks LoveService loveService;

    @Nested
    @DisplayName("좋아요 생성")
    class CreateLove{

        @Test
        @DisplayName("성공 테스트")
        public void successTest() throws Exception{
            //given
            Member member = Member.createMember("user", "pw", "nick");
            Board board = Board.createBoard("content", "picture", member);
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.ofNullable(member));
            given(boardRepository.findById(anyLong()))
                    .willReturn(Optional.ofNullable(board));
            given(loveRepository.isLoveByMember(anyLong(), anyLong())).willReturn(Optional.empty());
            //when
            //then
            assertDoesNotThrow(() -> loveService.createLove(1L, 2L));
        }

        @Test
        @DisplayName("해당하는 게시판 X")
        public void noBoardTest() throws Exception{
            //given
            Member member = Member.createMember("user", "pw", "nick");
            given(boardRepository.findById(anyLong()))
                    .willReturn(Optional.empty());

            //when //then
            assertThatThrownBy(() -> loveService.createLove(1L, 2L))
                    .isInstanceOf(PblException.class)
                    .hasMessageContaining("게시판");
        }
        @Test
        @DisplayName("해당하는 멤버 X")
        public void noMemberTest() throws Exception{
            //given
            Member member = Member.createMember("user", "pw", "nick");
            Board board = Board.createBoard("content", "picture", member);
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.empty());
            given(boardRepository.findById(anyLong()))
                    .willReturn(Optional.ofNullable(board));

            //when //then
            assertThatThrownBy(() -> loveService.createLove(1L, 2L))
                    .isInstanceOf(PblException.class)
                    .hasMessageContaining("사용자");
        }

        @Test
        @DisplayName("이미 좋아요를 누른 상태")
        public void aleradyLoveBoard() throws Exception{
            //given
            Member member = Member.createMember("user", "pw", "nick");
            Board board = Board.createBoard("content", "picture", member);
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.ofNullable(member));
            given(boardRepository.findById(anyLong()))
                    .willReturn(Optional.ofNullable(board));
            given(loveRepository.isLoveByMember(anyLong(), anyLong()))
                    .willReturn(Optional.ofNullable(Love.createLove(member, board)));

            //when //then
            assertThatThrownBy(() -> loveService.createLove(1L, 2L))
                    .isInstanceOf(PblException.class);
        }
    }

    @Nested
    @DisplayName("좋아요 삭제")
    class deleteLoveTest {

        @Test
        @DisplayName("정상 삭제 테스트")
        public void deleteSuccess() throws Exception {
            //given
            Member member = Member.createMember("user", "pw", "nick");
            Board board = Board.createBoard("content", "picture", member);
            given(boardRepository.findById(anyLong()))
                    .willReturn(Optional.ofNullable(board));
            given(loveRepository.isLoveByMember(anyLong(), anyLong()))
                    .willReturn(Optional.ofNullable(Love.createLove(member, board)));
            //when
            //then
            assertDoesNotThrow(() -> loveService.deleteLove(1L, 2L));
        }

        @Test
        @DisplayName("해당하는 게시판 없음")
        public void noBoardTest() throws Exception {
            //given
            Member member = Member.createMember("user", "pw", "nick");
            Board board = Board.createBoard("content", "picture", member);
            given(boardRepository.findById(anyLong()))
                    .willReturn(Optional.ofNullable(board));
            //when
            //then
            Assertions.assertThatThrownBy(() -> loveService.deleteLove(1L, 2L)).isInstanceOf(PblException.class);
        }

        @Test
        @DisplayName("좋아요를 누르지 않았을 때")
        public void notExistLove() throws Exception {
            //given
            Member member = Member.createMember("user", "pw", "nick");
            Board board = Board.createBoard("content", "picture", member);
            given(boardRepository.findById(anyLong()))
                    .willReturn(Optional.ofNullable(board));
            given(loveRepository.isLoveByMember(anyLong(), anyLong()))
                    .willReturn(Optional.empty());
            //when
            //then
            Assertions.assertThatThrownBy(() -> loveService.deleteLove(1L, 2L)).isInstanceOf(PblException.class);
        }
    }


}
