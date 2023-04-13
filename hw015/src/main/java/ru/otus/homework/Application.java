package ru.otus.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.otus.homework.services.AppRunner;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        AppRunner appRunner = context.getBean(AppRunner.class);
        appRunner.run();
    }
}
