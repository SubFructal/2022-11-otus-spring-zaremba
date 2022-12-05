package ru.otus.homework;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.homework.services.AppRunner;

import java.util.Scanner;

@PropertySource("classpath:app.properties")
@ComponentScan
@Configuration
public class App {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(App.class);
        var appRunner = context.getBean(AppRunner.class);
        appRunner.run();

    }
}
