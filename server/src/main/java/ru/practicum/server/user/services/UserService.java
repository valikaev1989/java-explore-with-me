package ru.practicum.server.user.services;

import org.springframework.data.domain.Pageable;
import ru.practicum.server.user.models.userDtos.UserInputDto;
import ru.practicum.server.user.models.userDtos.UserOutputDto;

import java.util.List;

public interface UserService {
    List<UserOutputDto> getAllUsers(List<Long> ids, Pageable page);

    UserOutputDto addUser(UserInputDto user);

    void deleteUser(Long userId);
}