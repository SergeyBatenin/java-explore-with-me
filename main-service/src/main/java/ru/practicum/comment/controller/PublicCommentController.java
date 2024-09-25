package ru.practicum.comment.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/events/{eventId}/comments")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicCommentController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getByEventId(@PathVariable @Positive long eventId,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                         @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("GET BY EVENT /comments/events/{} request", eventId);
        List<CommentDto> commentDtos = commentService.getByEventId(eventId, from, size);
        log.info("GET BY EVENT /comments/events/{} response: {} elements", eventId, commentDtos.size());
        return commentDtos;
    }
}
