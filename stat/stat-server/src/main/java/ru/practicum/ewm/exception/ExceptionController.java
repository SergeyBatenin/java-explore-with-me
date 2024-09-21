package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

@RestControllerAdvice
@Slf4j
public class ExceptionController extends RuntimeException {
    @ExceptionHandler({DateValidationException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage dateValidateHandle(Exception exception) {
        log.error("ERROR", exception);
        final ByteArrayOutputStream out = getOutputStream(exception);
        return new ErrorMessage(exception.getMessage(), out.toString(StandardCharsets.UTF_8));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handle(Exception exception) {
        log.error("ERROR", exception);
        final ByteArrayOutputStream out = getOutputStream(exception);
        return new ErrorMessage(exception.getMessage(), out.toString(StandardCharsets.UTF_8));
    }

    private static ByteArrayOutputStream getOutputStream(Exception exception) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        exception.printStackTrace(new PrintStream(out, true, StandardCharsets.UTF_8));
        return out;
    }
}
