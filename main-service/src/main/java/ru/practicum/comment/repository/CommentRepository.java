package ru.practicum.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.comment.model.Comment;
import ru.practicum.exception.DataNotFoundException;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByEventId(long eventId, Pageable page);

    default Comment checkAndGetComment(long commentId) {
        return this.findById(commentId)
                .orElseThrow(() -> {
//                    log.debug("GET COMMENT. Комментарий с айди {} не найден.", commentId);
                    return new DataNotFoundException("Комментарий с id=" + commentId + " не существует.");
                });
    }
}
