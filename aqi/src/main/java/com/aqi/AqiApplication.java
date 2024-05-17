package com.aqi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableJpaAuditing
@EnableScheduling
public class AqiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AqiApplication.class, args);
    }
}
