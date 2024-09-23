package ru.practicum.exception;

public class EventAvailableException extends RuntimeException {
    public EventAvailableException(String message) {
        super(message);
    }
}
