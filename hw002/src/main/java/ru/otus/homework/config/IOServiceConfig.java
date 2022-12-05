package ru.otus.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.homework.services.*;

@Configuration
public class IOServiceConfig {

    @Bean
    public IOService ioService() {
        return new IOServiceStreams(System.out, System.in);
    }

}
