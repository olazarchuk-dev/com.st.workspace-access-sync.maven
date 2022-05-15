package com.programming.techie.mongo;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMongock
public class WorkspaceSyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkspaceSyncApplication.class, args);
    }
}