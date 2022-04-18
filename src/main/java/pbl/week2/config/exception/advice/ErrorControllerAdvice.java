package pbl.week2.config.exception.advice;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ErrorControllerAdvice {
    @ExceptionHandler(AuthenticationException.class)
    public String authenticationException() {
        return "authentication error";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String accessDeniedException() {
        return "accessDeniedException error";
    }

    /**
     * @Valid 처리 ExceptionHandler
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String validtionException() {
        return "wroong input";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String illegalArgumentException() {
        return "IllegalArgumentException";
    }

    @ExceptionHandler(Exception.class)
    public String exception() {
        return "server errror";
    }
}
