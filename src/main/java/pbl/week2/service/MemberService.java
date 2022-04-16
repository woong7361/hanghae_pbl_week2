package pbl.week2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pbl.week2.entity.Member;
import pbl.week2.entity.entityDto.MemberDto;
import pbl.week2.repository.MemberRepository;
import pbl.week2.security.jwt.JwtTokenUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;


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
        boolean isUsername = memberRepository.findByUsername(registerDto.getUsername()).isPresent();
        boolean isNickname = memberRepository.findByNickname(registerDto.getNickname()).isPresent();
        if (isUsername || isNickname){
            throw new IllegalArgumentException("duplicate username or nickname");
        }

        String encodePassword = passwordEncoder.encode(registerDto.getPw());
        Member member = Member.createMember(registerDto.getUsername(), encodePassword, registerDto.getNickname());
        memberRepository.save(member);
    }

    public HttpHeaders login(MemberDto.Login loginDto) {
        Member member = memberRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(()->new IllegalArgumentException("멤버 없음"));

        if (!member.getUsername().equals(loginDto.getUsername())
                || !passwordEncoder.matches(loginDto.getPw(), member.getPw())) {
            throw new IllegalArgumentException("아이디 or 비밀번호 일치 X");
        }
        String token = "BEARER " + JwtTokenUtils.generateJwtToken(member);
        log.info("token = {}", token);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("Authorization", new ArrayList<>(Arrays.asList(token)));
        return new HttpHeaders(map);
    }
}
