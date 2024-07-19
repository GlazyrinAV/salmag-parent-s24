package ru.avg.managerapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ManagerAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerAppApplication.class, args);
    }

}
