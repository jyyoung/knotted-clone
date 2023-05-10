package com.knotted;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KnottedApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnottedApplication.class, args);
    }

}
