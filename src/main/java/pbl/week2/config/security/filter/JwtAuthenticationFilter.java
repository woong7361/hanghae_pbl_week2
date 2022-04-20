package pbl.week2.config.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;
import pbl.week2.config.exception.ErrorConstant;
import pbl.week2.config.security.PrincipalDetails;
import pbl.week2.entity.dto.ResultMsg;
import pbl.week2.entity.entityDto.MemberDto;
import pbl.week2.config.security.jwt.JwtTokenUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import static pbl.week2.config.exception.ErrorConstant.LOGIN_ERROR;
import static pbl.week2.config.security.jwt.JwtTokenUtils.*;


//스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음 - 로그인에 관련된 처리를 하는 필터이다.
//login 요청에서 username, password 전송하면 - 변경함
//usernamePasswordAuthenticationFilter가 동작한다
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
        super.setFilterProcessesUrl("/api/login");
    }


// /login 요청이 오면 로그인 시도를 위해서 실행되는 함수

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JWT AuthenticationFilter: 로그인 시도중");

        String jwtHeader = request.getHeader(TOKEN_HEADER_NAME);
        if (jwtHeader != null && jwtHeader.startsWith(TOKEN_NAME_WITH_SPACE)) {
            String jwtToken = getTokenFromHeader(request);
            String username = JwtTokenUtils.verifyToken(jwtToken)
                    .getClaim(CLAIM_USERNAME)
                    .asString();

            if (StringUtils.hasText(username)) {
                log.info("이미 로그인되어있는 유저입니다.");
                throw new DisabledException(LOGIN_ERROR);
            }
        }

        //1 get username, password
        try {
            ObjectMapper om = new ObjectMapper();
            MemberDto.Login loginMember = om.readValue(request.getInputStream(), MemberDto.Login.class);
            MemberDto.Session member= new MemberDto.Session(loginMember);

            if (!StringUtils.hasText(member.getUsername()) || !StringUtils.hasText(member.getPassword())) {
                log.info("아이디 혹은 비밀번호가 비어있습니다. {}", member);
                throw new BadCredentialsException(LOGIN_ERROR);
            }

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(member.getUsername(), member.getPassword());

            //매니저에게 생성한 토큰으로 로그인 시도 -> PrincipalDetailsService의 loadUserBuUserName() 함수가 실행
            //로그인 정보를 authentication에 저장 --- authentication에 UserDetails가 담겨있다.
            Authentication authentication =
                    this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);

            //return시 authentication객체가 session에 저장된다.
            // -> 세션에 저장하는 이유는 권한관리를 시큐리티가 해주기 때문
            return authentication;

        } catch (IOException e) {
            log.info("IOException!!!!!   {}", e.toString());
            throw new BadCredentialsException(LOGIN_ERROR);
        }
    }

    //attemptAuthentication실행 후 인증이 정상적으로 되었으면 successfulAuthentication 실행된다
    //JWT토큰을 만들어서 response해주면 된다
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principal = (PrincipalDetails) authResult.getPrincipal();

        //token에 id와 username 추가
        String jwtToken = JWT.create()
                .withSubject("sub")
                .withExpiresAt(new Date(System.currentTimeMillis() + (2 * HOUR) ))
                .withClaim(CLAIM_ID, principal.getMemberSession().getId())
                .withClaim(CLAIM_USERNAME, principal.getUsername())
                .sign(Algorithm.HMAC512(JWT_SECRET));   //secretkey

        response.addHeader(TOKEN_HEADER_NAME, TOKEN_NAME_WITH_SPACE + jwtToken);

        //response Body Msg추가
        ResultMsg error = new ResultMsg("success");
        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, error);
            os.flush();
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }

}
