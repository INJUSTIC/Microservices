package com.example.datagenerationservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Data Generation Service", version = "1.0", description = "This service generates data")
        /*servers = {
            @Server(url = "http://localhost:8080/data-generation", description = "Data generation service"),
            @Server(url = "http://localhost:8081/data-processing", description = "Data processing service"),
            @Server(url = "http://localhost:8082/collecting-metrics", description = "Collecting metrics service")
        }*/
)
public class DataGenerationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataGenerationServiceApplication.class, args);
    }
}
