package com.vranec.delimitation.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableAsync
public class DelimitationBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(DelimitationBackendApplication.class, args);
    }
}

// Add the controller.
@RestController
class HelloWorldController {
    @GetMapping("/")
    public String hello() {
        return "{\"hello\": \"world!\"}";
    }
}