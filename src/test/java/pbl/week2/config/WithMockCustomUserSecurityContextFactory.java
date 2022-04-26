package pbl.week2.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import pbl.week2.config.security.PrincipalDetails;
import pbl.week2.controller.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<config.WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(config.WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        PrincipalDetails principal = new PrincipalDetails(1L, "username", null);
        Authentication auth =
                new UsernamePasswordAuthenticationToken(principal, null, null);
        context.setAuthentication(auth);
        return context;
    }



}