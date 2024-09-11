package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConstraintViolation(DataIntegrityViolationException exception) {
        log.error("ERROR", exception);
        final ByteArrayOutputStream out = getOutputStream(exception);

        return ApiError.builder()
                .status("CONFLICT")
                .reason("Integrity constraint has been violated.")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .stacktrace(out.toString(StandardCharsets.UTF_8))
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handle(Exception exception) {
        log.error("ERROR", exception);
        final ByteArrayOutputStream out = getOutputStream(exception);

        return ApiError.builder()
                .status("INTERNAL_SERVER_ERROR")
                .reason("Что-то пошло не так.")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .stacktrace(out.toString(StandardCharsets.UTF_8))
                .build();
    }

    private static ByteArrayOutputStream getOutputStream(Exception exception) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        exception.printStackTrace(new PrintStream(out, true, StandardCharsets.UTF_8));
        return out;
    }
}
