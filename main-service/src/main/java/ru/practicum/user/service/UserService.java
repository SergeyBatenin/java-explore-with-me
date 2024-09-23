package ru.practicum.user.service;

import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserDto create(NewUserRequest newUser);

    List<UserDto> getByIds(Set<Long> userIds, int from, int size);

    void delete(long userId);
}
