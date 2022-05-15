package com.programming.techie.mongo;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMongock
public class DecompressPayloadSyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(DecompressPayloadSyncApplication.class, args);
    }
}
