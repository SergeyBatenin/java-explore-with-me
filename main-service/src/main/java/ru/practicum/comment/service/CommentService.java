package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.dto.UpdateCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto create(long userId, NewCommentDto newCommentDto);

    CommentDto update(long userId, long commentId, UpdateCommentDto updateCommentDto);

    void delete(long userId, long commentId);

    List<CommentDto> getByEventId(long eventId, int from, int size);
}
