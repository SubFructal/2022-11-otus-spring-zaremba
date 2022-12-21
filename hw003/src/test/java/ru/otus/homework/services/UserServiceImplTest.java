package ru.otus.homework.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.homework.domain.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("Класс UserServiceImpl")
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static final String TEST_VALUE = "testValue";

    @Mock
    private IOService ioService;

    @Mock
    private LocalizationMessageService messageService;

    @InjectMocks
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