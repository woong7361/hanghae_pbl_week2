package pbl.week2.config.exception.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pbl.week2.config.exception.ErrorConstant;
import pbl.week2.config.exception.PblException;
import pbl.week2.entity.dto.ResultMsg;

import static pbl.week2.config.exception.ErrorConstant.DEFAULT_ERROR;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ErrorControllerAdvice {

    private final MessageSource messageSource;

    /**
     * @Valid 처리 ExceptionHandler
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultMsg validtionException(MethodArgumentNotValidException e) {
        log.info("error message = {}",e.toString());
        log.info("error field = {}", e.getFieldError().getField());

        String message = messageSource.getMessage(DEFAULT_ERROR, null, null);
        return new ResultMsg(message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultMsg illegalArgumentException(IllegalArgumentException e) {
        log.info("error = {}",e.toString());

        String message = messageSource.getMessage(DEFAULT_ERROR, null, null);
        return new ResultMsg(message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultMsg exception(Exception e) {
        log.info("error = {}",e.toString());

        String message = messageSource.getMessage(DEFAULT_ERROR, null, null);
        return new ResultMsg(message);
    }

    @ExceptionHandler(PblException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultMsg PblException(PblException e) {
        log.info(e.getErrorLog());
        String message = messageSource.getMessage(e.getErrorLog(), null, null);

        return new ResultMsg(message);
    }
}
