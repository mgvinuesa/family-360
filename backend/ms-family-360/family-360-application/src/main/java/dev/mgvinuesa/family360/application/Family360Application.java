package dev.mgvinuesa.family360.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "dev.mgvinuesa.family360")
public class Family360Application {

    public static void main(String[] args) {
        SpringApplication.run(Family360Application.class, args);
    }
}
