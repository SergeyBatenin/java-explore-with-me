package ru.practicum.comment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.dto.UpdateCommentDto;
import ru.practicum.comment.service.CommentService;

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@PathVariable @Positive long userId,
                             @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("POST /user/{}/comments request: {}", userId, newCommentDto);
        CommentDto commentDto = commentService.create(userId, newCommentDto);
        log.info("POST /user/{}/comments response: {}", userId, commentDto);
        return commentDto;
    }

    @PatchMapping("/{commentId}")
    public CommentDto update(@PathVariable @Positive long userId,
                             @PathVariable @Positive long commentId,
                             @Valid @RequestBody UpdateCommentDto updateCommentDto) {
        log.info("PATCH /user/{}/comments/{} request: {}", userId, commentId, updateCommentDto);
        CommentDto commentDto = commentService.update(userId, commentId, updateCommentDto);
        log.info("PATCH /user/{}/comments/{} response: {}", userId, commentId, commentDto);
        return commentDto;
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive long userId, @PathVariable @Positive long commentId) {
        log.info("DELETE /user/{}/comments/{} request", userId, commentId);
        commentService.delete(userId, commentId);
        log.info("DELETE /user/{}/comments/{} response: success ", userId, commentId);
    }
}
