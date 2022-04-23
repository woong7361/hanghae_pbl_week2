package pbl.week2.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pbl.week2.config.exception.PblException;
import pbl.week2.entity.Member;
import pbl.week2.entity.entityDto.MemberDto;
import pbl.week2.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock private MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private MemberService createMemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        return new MemberService(memberRepository, passwordEncoder);
    }

    @Test()
    @DisplayName("회원가입 성공 테스트")
    public void registerSuccessTest() throws Exception{
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

}