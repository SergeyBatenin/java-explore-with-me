package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.mapper.UserMapper;
import ru.practicum.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserDto create(NewUserRequest newUser) {
        User user = userMapper.toUser(newUser);
        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getByIds(List<Long> userIds, int from, int size) {
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);

        List<User> users;
        if (userIds == null) {
            users = Optional.of(userRepository.findAll(page).getContent())
                    .orElseGet(Collections::emptyList);
        } else {
            users = userRepository.findAllByIdIn(userIds, page);
        }

        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void delete(long userId) {
        userRepository.deleteById(userId);
    }
}
