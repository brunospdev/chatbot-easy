package com.wpp.wppbotmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WppbotmanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WppbotmanagerApplication.class, args);
    }

}
