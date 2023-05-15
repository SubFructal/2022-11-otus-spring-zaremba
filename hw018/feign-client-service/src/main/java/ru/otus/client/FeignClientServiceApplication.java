package ru.otus.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableCircuitBreaker
@EnableDiscoveryClient
@EnableFeignClients
public class FeignClientServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FeignClientServiceApplication.class, args);

        System.out.println("http://localhost:8080/actuator");
    }
}
