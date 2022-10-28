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
import ru.practicum.server.event.model.EventDtos.EventFullDto;
import ru.practicum.server.event.model.EventDtos.EventInputDto;
import ru.practicum.server.event.services.EventService;
import ru.practicum.server.location.models.LocationDtos.LocationInputDto;
import ru.practicum.server.participationRequest.models.ParticipationDto.ParticipationRequestDto;
import ru.practicum.server.participationRequest.models.ParticipationRequest;
import ru.practicum.server.participationRequest.services.ParticipationService;
import ru.practicum.server.participationRequest.services.ParticipationServiceImpl;
import ru.practicum.server.user.controllers.UserControllerAdmin;
import ru.practicum.server.user.models.User;
import ru.practicum.server.user.models.userDtos.UserInputDto;
import ru.practicum.server.user.repositories.UserRepository;
import ru.practicum.server.user.services.UserService;
import ru.practicum.server.utils.State;
import ru.practicum.server.utils.Validation;

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
    private final UserRepository userRepository;
    private final Validation validation;
    private final ParticipationService participationService;
    private final EventService eventService;

    @Test
    void PostEvent() {
        userService.addUser(new UserInputDto("qwe", "qwe@qwe.ru"));
        userService.addUser(new UserInputDto("asd", "asd@asd.ru"));
        categoryService.addCategory(new CategoryDto(null, "qwe"));
        EventInputDto eventInputDto = new EventInputDto(
                null, "annot", 1L, "desc", "2022-10-29 05:42:30",
                new LocationInputDto(847F, 645F), true, 564, false, "title", State.PUBLISHED.toString());
        EventFullDto eventFullDto = eventService.addEvent(1L, eventInputDto);
        ParticipationRequestDto requestDto = participationService.addUserRequestParticipation(2L, 1L);
        System.out.println(requestDto);
        System.out.println(eventFullDto);
        System.out.println(eventService.getEventByIdForPublic(1L));
    }

    @Test
    void contextLoads() {
    }

}
