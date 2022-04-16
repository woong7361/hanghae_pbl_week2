package pbl.week2.security.filter;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import pbl.week2.entity.Member;
import pbl.week2.repository.MemberRepository;
import pbl.week2.security.PrincipalDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static pbl.week2.security.jwt.JwtTokenUtils.*;

//시큐리티 필터중 BasicAuthenticationFilter가 있다
//권한이나 인증이 필요한 특정 주소를 입력했을 때 위 필터를 무조건 탄다
//만약 권한이나 인증이 필요한 주소가 아니라면 이 필터를 타지 않는다.
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final MemberRepository memberRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
    }

    //인증이나 권한이 필요하면 doFilterInternal 탄다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwtHeader = request.getHeader(TOKEN_HEADER_NAME);

        //헤더가 있는지 확인
        if (jwtHeader == null || !jwtHeader.startsWith(TOKEN_NAME_WITH_SPACE)) {
            log.info("no header request");
            chain.doFilter(request, response);
            return;
        }
        String jwtToken = request.getHeader(TOKEN_HEADER_NAME).replace(TOKEN_NAME_WITH_SPACE, "");

        //jwt Decoding & verify - 시간까지 라이브러리가 자동 검증 https://github.com/auth0/java-jwt
        String username = JWT
                .require(Algorithm.HMAC512(JWT_SECRET))
                .build()
                .verify(jwtToken)
                .getClaim(CLAIM_USERNAME)
                .asString();

        if (StringUtils.hasText(username)) {

            //!!!!!!!!!!!!!!!!!!!!!!!!!!!! repository 사용 여부 - 1 access = 1 query !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!/
            Member member = memberRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("회원가입 되지 않은 회원입니다."));
            log.warn("권한 접근 한번당 한번의 쿼리가 발생하고 있습니다!!!!!!!!");

            PrincipalDetails principalDetails = new PrincipalDetails(member.getId(), member.getUsername(), member.getPw());

            //토큰을 강제로 생성 - 서비스를 통해 로그인을 할것이 아니기 때문에 비밀번호는 null이 가능하다.
            //username이 null이 아니기 때문에 authentication 객체를 만들어도 된다. -> 인증이 된 부분
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            //강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}
