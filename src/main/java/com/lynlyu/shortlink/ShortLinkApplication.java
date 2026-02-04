package com.lynlyu.shortlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableAsync
@EnableScheduling
public class ShortLinkApplication {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        SpringApplication.run(ShortLinkApplication.class, args);

        long endTime = System.currentTimeMillis();
        System.out.println("------------------------------------------------");
        System.out.println("ShortLink Service Started Successfully!");
        System.out.println("Startup time: " + (endTime - startTime) + "ms");
        System.out.println("------------------------------------------------");
    }
}