package com.hy.haeyoback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class HaeyoBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(HaeyoBackApplication.class, args);
    }

}
