package pbl.week2.controller;

import org.springframework.security.test.context.support.WithSecurityContext;
import pbl.week2.config.WithMockCustomUserSecurityContextFactory;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class config {
    @Retention(RetentionPolicy.RUNTIME)
    @WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
    public @interface WithMockCustomUser {

        String username() default "rob";

        String name() default "Rob Winch";
    }
}
