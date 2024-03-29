package com.example.datagenerationservice.controller;

import com.example.datagenerationservice.model.PersonData;
import com.example.datagenerationservice.service.DataGenerationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DataGenerationController {
    private final DataGenerationService dataGenerationService;

    @Autowired
    public DataGenerationController(DataGenerationService dataGenerationService) {
        this.dataGenerationService = dataGenerationService;
    }

    @Operation(summary = "Generate json", description = "Generates x=size json objects with random data")
    @GetMapping("/generate/json/{size}")
    public List<PersonData> generateJson(@PathVariable int size) {
        if (size < 0) throw new IllegalArgumentException("size is negative");
        return dataGenerationService.generateRandomPersonDataList(size);
    }

}
