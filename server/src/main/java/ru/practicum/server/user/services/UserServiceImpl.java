package ru.practicum.server.user.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.user.models.userDtos.UserMapper;
import ru.practicum.server.user.models.userDtos.UserInputDto;
import ru.practicum.server.user.models.userDtos.UserOutputDto;
import ru.practicum.server.user.repositories.UserRepository;
import ru.practicum.server.utils.UserValidator;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserValidator validator;

    @Override
    public List<UserOutputDto> getAllUsers(List<Long> ids, Pageable page) {
        log.info("getAllUsers start: ids:{}, page: {}.", ids, page);
        List<UserOutputDto> userOutputDtoList = checkListUserIds(ids, page);
        log.info("getAllUsers end: userOutputDtoList:{}.", userOutputDtoList);
        return userOutputDtoList;
    }

    @Override
    @Transactional
    public UserOutputDto addUser(UserInputDto userInputDto) {
        log.info("addUser start: userInputDto:{}.", userInputDto);
        UserOutputDto userOutputDto = UserMapper.toDto(userRepository.save(UserMapper.toUser(userInputDto)));
        log.info("addUser end: userOutputDto:{}.", userOutputDto);
        return userOutputDto;
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.info("deleteUser start: userId:{}.", userId);
        validator.validateAndReturnUserByUserId(userId);
        userRepository.deleteById(userId);
        log.info("deleteUser end userId:{}.", userId);
    }

    private List<UserOutputDto> checkListUserIds(List<Long> ids, Pageable pageable) {
        if (ids.isEmpty()) {
            return UserMapper.toDtoUsers(userRepository.findAll(pageable));
        } else {
            return UserMapper.toDtoUsers(
                    userRepository.findUserByUserIdIn(validator.getCorrectUserIdList(ids), pageable));
        }
    }
}