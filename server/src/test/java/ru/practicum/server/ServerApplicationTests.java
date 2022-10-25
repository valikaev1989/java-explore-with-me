package ru.practicum.server;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.server.category.model.categoryDtos.CategoryDto;
import ru.practicum.server.category.services.CategoryService;
import ru.practicum.server.compilation.controllers.CompilationControllerAdmin;
import ru.practicum.server.event.controllers.EventControllerAdmin;
import ru.practicum.server.event.controllers.EventControllerPrivate;
import ru.practicum.server.event.controllers.EventControllerPublic;
import ru.practicum.server.event.model.EventDtos.EventInputDto;
import ru.practicum.server.location.models.LocationDtos.LocationInputDto;
import ru.practicum.server.user.controllers.UserControllerAdmin;
import ru.practicum.server.user.models.userDtos.UserInputDto;
import ru.practicum.server.user.services.UserService;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ServerApplicationTests {

    private final EventControllerPublic eventControllerPublic;
    private final EventControllerPrivate eventControllerPrivate;
    private final CategoryService categoryService;
    private final UserService userService;
    private final UserControllerAdmin userControllerAdmin;
    private final CompilationControllerAdmin compilationControllerAdmin;
    private final EventControllerAdmin eController;

    @Test
    void PostEvent() {
        userService.addUser(new UserInputDto("qwe", "qwe@qwe.ru"));
        categoryService.addCategory(new CategoryDto(null, "qwe"));
        EventInputDto eventInputDto = new EventInputDto(
                null, "annot", 1L, "desc", "2022-10-25 05:42:30",
                new LocationInputDto(847F, 645F), true, 564, true, "title", null);
        eventControllerPrivate.addEvent(1L, eventInputDto);
    }

    @Test
    void contextLoads() {
    }

}
