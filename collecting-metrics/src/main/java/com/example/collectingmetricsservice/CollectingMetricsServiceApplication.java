package com.example.collectingmetricsservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Data Generation Service", version = "1.0", description = "This service collects and shows metrics (used RAM and time of each service)"))
public class CollectingMetricsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CollectingMetricsServiceApplication.class, args);
	}
}
