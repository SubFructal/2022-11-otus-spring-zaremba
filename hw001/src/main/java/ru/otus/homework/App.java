package ru.otus.homework;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.homework.services.AppRunner;

public class App {
    public static void main(String[] args) {
        var context = new ClassPathXmlApplicationContext("/spring-context.xml");
        var appRunner = context.getBean(AppRunner.class);
        appRunner.run();
    }
}
