package ru.practicum.server.user.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.user.models.userDtos.UserInputDto;
import ru.practicum.server.user.models.userDtos.UserOutputDto;
import ru.practicum.server.user.services.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.server.utils.FormatPage.getPage;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserControllerAdmin {
    private final UserService userService;

    @GetMapping
    public List<UserOutputDto> getUsersFromListIds(
            @RequestParam(required = false, defaultValue = "List.of()") List<Long> ids,
            @RequestParam(value = "from", required = false, defaultValue = "0") @Min(0) Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") @Min(0) Integer size) {
        log.info("getUsersFromListIds: ids: {}, from: {},size: {}", ids, from, size);
        return userService.getAllUsers(ids, getPage(from, size));
    }

    @PostMapping
    public UserOutputDto addUser(@Valid @RequestBody UserInputDto userInputDto) {
        log.info("addUser: userInputDto: {}", userInputDto);
        return userService.addUser(userInputDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @Min(0) Long userId) {
        log.info("deleteUser: userId: {}", userId);
        userService.deleteUser(userId);
    }
}