package pbl.week2.config.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class CustomAuthenticationException extends AuthenticationException {
    private final String errorLog;
    private final String errorCode;

    public CustomAuthenticationException(String errorLog, String errorCode) {
        super(errorLog);
        this.errorLog = errorLog;
        this.errorCode = errorCode;
    }
}



