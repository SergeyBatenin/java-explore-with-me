package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.dto.UpdateCommentDto;
import ru.practicum.comment.dto.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.exception.NotAvailableException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Transactional
    @Override
    public CommentDto create(long userId, NewCommentDto newCommentDto) {
        User user = checkAndGetUser(userId);
        Event event = checkAndGetEvent(newCommentDto.getEventId());

        Comment comment = commentMapper.toComment(newCommentDto, event, user);
        System.out.println("\n" + comment + "\n");
        commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }

    @Transactional
    @Override
    public CommentDto update(long userId, long commentId, UpdateCommentDto updateCommentDto) {
        checkAndGetUser(userId);
        Comment comment = checkAndGetComment(commentId);
        if (comment.getAuthor().getId() != userId) {
            throw new NotAvailableException("Изменить комментарий может только автор.");
        }
        comment.setText(updateCommentDto.getText());

        return commentMapper.toDto(comment);
    }

    @Transactional
    @Override
    public void delete(long userId, long commentId) {
        checkAndGetUser(userId);
        Comment comment = checkAndGetComment(commentId);
        if (comment.getAuthor().getId() != userId) {
            throw new NotAvailableException("Удалить комментарий может только автор.");
        }
        commentRepository.deleteById(commentId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> getByEventId(long eventId, int from, int size) {
        checkAndGetEvent(eventId);
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);

        return commentRepository.findByEventId(eventId, page).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    private User checkAndGetUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.debug("GET USER. Пользователь с айди {} не найден.", userId);
                    return new DataNotFoundException("Пользователь с id=" + userId + " не существует.");
                });
    }

    private Event checkAndGetEvent(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    log.debug("GET USER. Событие с айди {} не найден.", eventId);
                    return new DataNotFoundException("Событие с id=" + eventId + " не существует.");
                });
    }

    private Comment checkAndGetComment(long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    log.debug("GET COMMENT. Комментарий с айди {} не найден.", commentId);
                    return new DataNotFoundException("Комментарий с id=" + commentId + " не существует.");
                });
    }
}
