package ru.practicum.server.user.models.userDtos;

import org.springframework.data.domain.Page;
import ru.practicum.server.user.models.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static User toUser(UserInputDto userInputDto) {
        if (userInputDto == null) {
            return null;
        }
        return User.builder()
                .name(userInputDto.getName())
                .email(userInputDto.getEmail())
                .build();
    }

    public static UserOutputDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return UserOutputDto.builder()
                .id(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static List<UserOutputDto> toDtoUsers(Page<User> userPage) {
        return userPage.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }
}