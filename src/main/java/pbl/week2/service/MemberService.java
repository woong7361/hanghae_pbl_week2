package pbl.week2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pbl.week2.config.exception.ErrorConstant;
import pbl.week2.entity.Member;
import pbl.week2.entity.entityDto.MemberDto;
import pbl.week2.repository.MemberRepository;


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
            log.info("아이디와 비밀번호가 같으면 안됩니다");
            throw new IllegalArgumentException(ErrorConstant.DEFAULT_ERROR);
        }
    }

    private void isDuplicateUsernameAndNickname(MemberDto.Register registerDto) {
        boolean isUsername = memberRepository.findByUsername(registerDto.getUsername()).isPresent();
        boolean isNickname = memberRepository.findByNickname(registerDto.getNickname()).isPresent();
        if (isUsername || isNickname){
            log.info("중복된 아이디 또는 닉네임이 존재합니다.");
            throw new IllegalArgumentException(ErrorConstant.DEFAULT_ERROR);
        }
    }
}
