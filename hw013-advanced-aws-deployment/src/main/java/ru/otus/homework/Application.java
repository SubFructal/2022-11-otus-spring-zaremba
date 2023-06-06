package ru.otus.homework;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        var ctx = SpringApplication.run(Application.class, args);
        var activeProfile = ctx.getBean(ActiveProfile.class);

        System.out.printf("%nАктивный профиль: %s; порт: %s%n", activeProfile.getActiveProfile(), activeProfile.getServerPort());
        System.out.printf("%nГлавная dev: %n%s%n", "http://localhost:8080");
        System.out.printf("%nГлавная prod: %n%s%n", "http://localhost:5000");
        System.out.printf("%nлогин: %s; пароль: %s%n", "admin", "admin123");
        System.out.printf("логин: %s; пароль: %s%n%n", "user", "user456");
    }

    @Component
    @Getter
    public static class ActiveProfile {
        private final String activeProfile;
        private final int serverPort;

        public ActiveProfile(@Value("${spring.profiles.active}") String activeProfile, @Value("${server.port}") int serverPort) {
            this.activeProfile = activeProfile;
            this.serverPort = serverPort;
        }
    }
}
