package com.dm.labs.ryanairwebscrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RyanairWebscrapperApplication {

    public static void main(String[] args) {
        SpringApplication.run(RyanairWebscrapperApplication.class, args);
    }

}
