package ru.otus.homework.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.User;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String MSG_INTRODUCE = "Please introduce yourself";
    private static final String MSG_ENTER_FIRST_NAME = "Enter your first name:";
    private static final String MSG_ENTER_LAST_NAME = "Enter your last name:";

    private final IOService ioService;

    @Override
    public User getUser() {
        ioService.output(MSG_INTRODUCE);
        String firstName = ioService.readLn(MSG_ENTER_FIRST_NAME);
        String lastName = ioService.readLn(MSG_ENTER_LAST_NAME);
        return new User(firstName, lastName);
    }
}
