package ru.practicum.user.service;

import org.springframework.stereotype.Component;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import java.util.List;

@Component
public interface UserService {
    UserDto create(NewUserRequest newUser);

    List<UserDto> getByIds(List<Long> userIds, int from, int size);

    void delete(long userId);
}
