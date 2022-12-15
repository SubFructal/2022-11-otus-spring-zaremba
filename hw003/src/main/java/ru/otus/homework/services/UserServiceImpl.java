package ru.otus.homework.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.User;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final IOService ioService;
    private final LocalizationMessageService messageService;

    @Override
    public User getUser() {
        ioService.output(messageService.getLocalizedMessage("message.introduce"));
        String firstName = ioService.readLn(messageService.getLocalizedMessage("message.enter.first.name"));
        String lastName = ioService.readLn(messageService.getLocalizedMessage("message.enter.last.name"));
        return new User(firstName, lastName);
    }
}
