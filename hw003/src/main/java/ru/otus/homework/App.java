package ru.otus.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.homework.services.AppRunner;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        var context = SpringApplication.run(App.class, args);
        var appRunner = context.getBean(AppRunner.class);
        appRunner.run();
    }
}
