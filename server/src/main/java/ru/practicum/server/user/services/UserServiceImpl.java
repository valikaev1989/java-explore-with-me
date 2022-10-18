package ru.practicum.server.user.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.server.exception.models.NotFoundException;
import ru.practicum.server.user.models.User;
import ru.practicum.server.user.models.userDtos.UserMapper;
import ru.practicum.server.user.models.userDtos.UserInputDto;
import ru.practicum.server.user.models.userDtos.UserOutputDto;
import ru.practicum.server.user.repositories.UserRepository;
import ru.practicum.server.utils.Validation;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Validation validation;

    @Override
    public List<UserOutputDto> getAllUsers(List<Long> ids, Pageable page) {
        log.info("UserService.getAllUsers start: ids:{}, page: {}.", ids, page);
        List<UserOutputDto> userOutputDtoList;
        if (ids != null || !ids.isEmpty()) {
            userOutputDtoList = UserMapper.toDtoUsers(userRepository.findAll(page));
        } else {
            userOutputDtoList = UserMapper.toDtoUsers(userRepository.findUserByUserIdIn(getCorrectIdList(ids), page));
        }
        log.info("UserService.getAllUsers end: userOutputDtoList:{}.", userOutputDtoList);
        return userOutputDtoList;
    }

    @Override
    public UserOutputDto addUser(UserInputDto userInputDto) {
        log.info("UserService.addUser start: userInputDto:{}.", userInputDto);
        UserOutputDto userOutputDto = UserMapper.toDto(userRepository.save(UserMapper.toUser(userInputDto)));
        log.info("UserService.addUser end: userOutputDto:{}.", userOutputDto);
        return userOutputDto;
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("UserService.deleteUser start: userId:{}.", userId);
        validation.validateAndReturnUserByUserId(userId);
        userRepository.deleteById(userId);
        log.info("UserService.deleteUser end userId:{}.", userId);
    }

    private List<Long> getCorrectIdList(List<Long> ids) {
        List<Long> correct = new ArrayList<>();
        for (Long id : ids) {
            try {
                User user = validation.validateAndReturnUserByUserId(id);
                correct.add(user.getUserId());
            } catch (NotFoundException ex) {
                log.warn(ex.getMessage());
            }
        }
        return correct;
    }
}