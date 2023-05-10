package ru.otus.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
//import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableCircuitBreaker
@EnableHystrixDashboard
//@EnableTurbine
@EnableFeignClients
public class FeignClientServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FeignClientServiceApplication.class, args);

        System.out.println("http://localhost:8080/hystrix");
        System.out.println();
        System.out.println("localhost:8080/actuator/hystrix.stream");
        System.out.println();
        System.out.println("http://localhost:8080/actuator");
    }
}
