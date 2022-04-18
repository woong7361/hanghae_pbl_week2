package pbl.week2.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pbl.week2.entity.Member;
import pbl.week2.entity.entityDto.MemberDto;
import pbl.week2.repository.MemberRepository;
import pbl.week2.security.jwt.JwtTokenUtils;

import java.util.Date;

import static pbl.week2.security.jwt.JwtTokenUtils.JWT_SECRET;
import static pbl.week2.security.jwt.JwtTokenUtils.TOKEN_NAME_WITH_SPACE;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(MemberDto.Register registerDto) {
        //중복된 닉네임 아이디 확인
        isDuplicateUsernameAndNickname(registerDto);
        isUsernameSameAsPassword(registerDto);

        registerDto.setPw(passwordEncoder.encode(registerDto.getPw()));
        Member member = Member.createMemberByRegister(registerDto);
        memberRepository.save(member);
    }

    private void isUsernameSameAsPassword(MemberDto.Register registerDto) {
        if (registerDto.getUsername().equals(registerDto.getPw())) {
            throw new IllegalArgumentException("아이디와 비밀번호가 같으면 안됩니다");
        }
    }

    private void isDuplicateUsernameAndNickname(MemberDto.Register registerDto) {
        boolean isUsername = memberRepository.findByUsername(registerDto.getUsername()).isPresent();
        boolean isNickname = memberRepository.findByNickname(registerDto.getNickname()).isPresent();
        if (isUsername || isNickname){
            throw new IllegalArgumentException("duplicate username or nickname");
        }
    }
}
