package ru.otus.homework.actuators;

import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.homework.services.BookService;

@Component
@AllArgsConstructor
public class LibraryStatusHealthIndicator implements HealthIndicator {

    private final BookService bookService;

    @Override
    public Health health() {
        var booksCount = bookService.getBooksCount();
        boolean isEmpty = booksCount == 0;
        if (isEmpty) {
            return Health
                    .status(Status.DOWN)
                    .withDetail("message", "В библиотеке нет книг, проверьте, все ли в порядке!")
                    .build();
        } else {
            return Health
                    .status(Status.UP)
                    .withDetail("message", "Все в порядке")
                    .build();
        }
    }
}
