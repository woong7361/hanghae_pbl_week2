package pbl.week2.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pbl.week2.entity.Member;
import pbl.week2.repository.MemberRepository;

import static pbl.week2.config.exception.ErrorConstant.LOGIN_ERROR;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.info("로그인 요청한 회원이 DB에 없다");
                    return new UsernameNotFoundException(LOGIN_ERROR);
                });

        return new PrincipalDetails(member.getId(), member.getUsername(), member.getPw());
    }
}
