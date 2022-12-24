package ru.otus.homework.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.homework.services.IOService;
import ru.otus.homework.services.IOServiceStreams;

@Configuration
public class IOServiceConfig {

    @Bean
    public IOService ioService() {
        return new IOServiceStreams(System.out, System.in);
    }

}
