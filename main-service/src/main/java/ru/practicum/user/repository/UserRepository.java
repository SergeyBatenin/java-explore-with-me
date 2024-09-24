package ru.practicum.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByIdIn(Set<Long> ids, Pageable page);

    default User checkAndGetUser(long userId) {
        return this.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь с id=" + userId + " не существует."));
    }
}
