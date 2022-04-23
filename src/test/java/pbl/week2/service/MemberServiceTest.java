package pbl.week2.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pbl.week2.config.exception.PblException;
import pbl.week2.entity.Member;
import pbl.week2.entity.entityDto.MemberDto;
import pbl.week2.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock private MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private MemberService createMemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        return new MemberService(memberRepository, passwordEncoder);
    }

    @Nested()
    @DisplayName("회원가입_테스트")
    class RegisterTest {
        @Test()
        @DisplayName("성공 테스트")
        public void SuccessTest() throws Exception{
            //given
            MemberService memberService = createMemberService(memberRepository, passwordEncoder);
            MemberDto.Register registerDto = new MemberDto.Register("member", "password", "nickname");
            given(memberRepository.findByUsername(registerDto.getUsername())).willReturn(Optional.empty());
            given(memberRepository.findByNickname(registerDto.getNickname())).willReturn(Optional.empty());
            //when
            memberService.register(registerDto);
            //then
            assertDoesNotThrow(() -> memberService.register(registerDto));
        }
        @Test()
        @DisplayName("중복 닉네임 테스트")
        public void duplicateNickname() throws Exception{
            //given
            MemberService memberService = createMemberService(memberRepository, passwordEncoder);
            MemberDto.Register registerDto = new MemberDto.Register("member", "password", "nickname");
            given(memberRepository.findByUsername(registerDto.getUsername())).willReturn(Optional.empty());
            given(memberRepository.findByNickname(registerDto.getNickname()))
                    .willReturn(Optional.ofNullable(Member.createMemberByRegister(registerDto)));
            //when
            //then
            assertThatThrownBy(() -> memberService.register(registerDto)).isInstanceOf(PblException.class);
        }
        @Test()
        @DisplayName("중복 Username 테스트")
        public void duplicateUsernameTest() throws Exception{
            //given
            MemberService memberService = createMemberService(memberRepository, passwordEncoder);
            MemberDto.Register registerDto = new MemberDto.Register("member", "password", "nickname");
            given(memberRepository.findByUsername(registerDto.getUsername()))
                    .willReturn(Optional.ofNullable(Member.createMemberByRegister(registerDto)));

            given(memberRepository.findByNickname(registerDto.getNickname())).willReturn(Optional.empty());
            //when
            //then
            assertThatThrownBy(() -> memberService.register(registerDto)).isInstanceOf(PblException.class);
        }

        @Test()
        @DisplayName("비밀번호와 username동일 테스트")
        public void sameusernamePassword() throws Exception{
            //given
            MemberService memberService = createMemberService(memberRepository, passwordEncoder);
            MemberDto.Register registerDto = new MemberDto.Register("member", "member", "nickname");
            given(memberRepository.findByUsername(registerDto.getUsername())).willReturn(Optional.empty());
            given(memberRepository.findByNickname(registerDto.getNickname())).willReturn(Optional.empty());
            //when
            //then
            assertThatThrownBy(() -> memberService.register(registerDto)).isInstanceOf(PblException.class);
        }

        @Test()
        @DisplayName("비밀번호 암호화 테스트")
        public void passwordEncoderTest() throws Exception{
            //given
            String password = "password";
            //when
            String encodedPassword = passwordEncoder.encode(password);
            //then
            assertThat(password).isNotEqualTo(encodedPassword);
            assertThat(passwordEncoder.matches(password, encodedPassword)).isTrue();
        }


    }


}