package pbl.week2.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pbl.week2.config.security.exceptionhandler.CustomAuthenticationEntryPoint;
import pbl.week2.config.security.filter.JwtAuthenticationFilter;
import pbl.week2.config.security.filter.JwtAuthorizationFilter;
import pbl.week2.repository.MemberRepository;
import pbl.week2.config.security.exceptionhandler.AuthenticationFailureHandlerImpl;
import pbl.week2.config.security.exceptionhandler.CustomAccessDeniedHandler;

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
        http.cors().configurationSource(corsConfigurationSource())
                .and().csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(new CustomAccessDeniedHandler());

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable() //form으로 로그인 안함
                .addFilter(jwtAuthenticationFilter(authenticationManager()))// authentication manager를 파라미터로 넘겨주어야 한다
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), authenticationEntryPoint, memberRepository))
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/api/*").permitAll() //option method 허락
                .antMatchers("/api/posts").permitAll()
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}
