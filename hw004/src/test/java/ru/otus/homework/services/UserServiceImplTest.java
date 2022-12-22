package ru.otus.homework.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.homework.domain.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("Класс UserServiceImpl")
@SpringBootTest
class UserServiceImplTest {
    private static final String TEST_VALUE = "testValue";

    @MockBean
    private IOService ioService;

    @MockBean
    private LocalizationMessageService messageService;

    @Autowired
    private UserServiceImpl userService;

    @DisplayName("должен возвращать корректный объект класса User")
    @Test
    void shouldReturnCorrectInstanceOfUserClass() {
        var expectedUser = new User(TEST_VALUE, TEST_VALUE);
        given(ioService.readLn(any())).willReturn(TEST_VALUE);

        assertThat(userService.getUser())
                .usingRecursiveComparison()
                .isEqualTo(expectedUser);
    }
}