package com.berkerol.joblex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class JoblexApplication {

    public static void main(String[] args) {
        SpringApplication.run(JoblexApplication.class, args);
    }

}
