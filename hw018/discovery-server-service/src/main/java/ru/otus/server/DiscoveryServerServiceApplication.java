package ru.otus.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServerServiceApplication.class, args);

        System.out.println("http://localhost:8761");
    }
}
