package ru.practicum.server.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.server.exception.models.NotFoundException;
import ru.practicum.server.user.models.User;
import ru.practicum.server.user.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    public User validateAndReturnUserByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("пользователь с id '%d' не найден", userId)));
    }

    public List<Long> getCorrectUserIdList(List<Long> ids) {
        log.info("Validation.getCorrectUserIdList start: ids: {}", ids);
        List<Long> userIds = new ArrayList<>();
        for (Long id : ids) {
            try {
                User user = validateAndReturnUserByUserId(id);
                userIds.add(user.getUserId());
            } catch (NotFoundException ex) {
                log.warn(ex.getMessage());
            }
        }
        log.info("Validation.getCorrectUserIdList end: ids: {}", userIds);
        return userIds;
    }
}