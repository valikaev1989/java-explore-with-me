package ru.practicum.server.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.category.repositories.CategoryRepository;
import ru.practicum.server.exception.models.NotFoundException;
import ru.practicum.server.location.models.Location;
import ru.practicum.server.location.repositories.LocationRepository;
import ru.practicum.server.user.models.User;
import ru.practicum.server.user.repositories.UserRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class Validation {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    public User validateAndReturnUserByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format(
                "пользователь с id '%d' не найден", userId)));
    }

    public Category validateAndReturnCategoryByCategoryId(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException(String.format(
                "категория с id '%d' не найдена", categoryId)));
    }

    public Location validateAndReturnLocationByLocationId(Long locationId) {
        return locationRepository.findById(locationId).orElseThrow(() -> new NotFoundException(String.format(
                "локация с id '%d' не найдена", locationId)));
    }
}