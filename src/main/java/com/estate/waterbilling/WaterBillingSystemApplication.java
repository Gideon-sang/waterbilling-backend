package com.estate.waterbilling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WaterBillingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaterBillingSystemApplication.class, args);
    }
}
