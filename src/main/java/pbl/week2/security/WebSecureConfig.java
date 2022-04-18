package pbl.week2.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import pbl.week2.repository.MemberRepository;
import pbl.week2.security.exceptionhandler.AuthenticationFailureHandlerImpl;
import pbl.week2.security.exceptionhandler.CustomAccessDeniedHandler;
import pbl.week2.security.exceptionhandler.CustomAuthenticationEntryPoint;
import pbl.week2.security.filter.JwtAuthenticationFilter;
import pbl.week2.security.filter.JwtAuthorizationFilter;

@EnableWebSecurity
@Configurable
@RequiredArgsConstructor
public class WebSecureConfig extends WebSecurityConfigurerAdapter {

    private final MemberRepository memberRepository;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final AuthenticationFailureHandlerImpl authenticationFailureHandler;

    //이걸로 안막으면 인증이 필요없는 부분도 필터를 탐 (forbidden으로 막히지는 않음)
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/api/register");
//                .antMatchers("/api/login");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(new CustomAccessDeniedHandler());

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable() //form으로 로그인 안함
                .httpBasic().disable() //http basic 방식 사용 안함
                .addFilter(jwtAuthenticationFilter(authenticationManager()))// authentication manager를 파라미터로 넘겨주어야 한다
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), memberRepository))
                .authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .anyRequest().permitAll();

//                .antMatchers("/api/v1/user/**")
//                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
//                .antMatchers("/api/v1/manager/**")
//                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
//                .antMatchers("/api/v1/admin/**")
//                .access("hasRole('ROLE_ADMIN')")
//                .anyRequest().permitAll();

    }

    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager);
        jwtAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return jwtAuthenticationFilter;
    }

}
