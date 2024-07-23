package ru.raccoon.netologydiploma;

import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Runner implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Runner.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
    }
}
