package ru.practicum.user.service;

import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(NewUserRequest newUser);

    List<UserDto> getByIds(List<Long> userIds, int from, int size);

    void delete(long userId);
}
