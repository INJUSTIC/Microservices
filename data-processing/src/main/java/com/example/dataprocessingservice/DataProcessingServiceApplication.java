package com.example.dataprocessingservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Data Processing Service", version = "1.0", description = "This service processes data from data generation service"))
public class DataProcessingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataProcessingServiceApplication.class, args);
    }

}
