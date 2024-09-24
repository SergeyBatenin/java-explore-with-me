package ru.practicum.comment.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.model.Event;
import ru.practicum.user.dto.mapper.UserMapper;
import ru.practicum.user.model.User;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    Comment toComment(NewCommentDto commentDto, Event event, User author);

    @Mapping(target = "eventId", source = "comment.event.id")
    CommentDto toDto(Comment comment);
}
