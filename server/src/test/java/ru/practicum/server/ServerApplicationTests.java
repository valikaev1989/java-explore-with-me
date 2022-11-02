package ru.practicum.server;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.server.user.models.User;
import ru.practicum.server.user.repositories.UserRepository;
import ru.practicum.server.utils.CommentValidator;

import java.time.LocalDateTime;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ServerApplicationTests {
    private final UserRepository userRepository;
    private final CommentValidator commentValidator;

    @Test
    void contextLoads() {
//        User user = User.builder()
//                .name("qwe")
//                .email("qwe@qwe.ru")
//                .permissionToComment(true)
//                .blockComments(LocalDateTime.now().plusHours(1).plusMinutes(20))
//                .build();
//        userRepository.save(user);
//        System.out.println(LocalDateTime.now());
//        commentValidator.validateBlockCommentingByUser(user);
    }
}