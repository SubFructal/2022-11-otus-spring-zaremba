package ru.otus.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        System.out.printf("%nГлавная: %n%s%n", "http://localhost:8080");
        System.out.printf("логин: %s; пароль: %s%n", "admin", "admin123");
        System.out.printf("логин: %s; пароль: %s%n%n", "user", "user456");

    }
}
