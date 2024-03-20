package com.example.dataprocessingservice.controller;
import com.example.dataprocessingservice.service.DataProcessingService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@RestController
public class DataProcessingController {
    private final DataProcessingService dataProcessingService;

    @Autowired
    public DataProcessingController(DataProcessingService dataProcessingService) {
        this.dataProcessingService = dataProcessingService;
    }

    //zwraca dane w formacie csv z kolumnami ‘type, _id, name, type, latitude, longitude’
    @Operation(summary = "convert to csv", description = "generates x=size random json object and converts them to csv format")
    @GetMapping("/data/csv/{size}")
    public String convertToCSV(@PathVariable int size) {
        return dataProcessingService.convertToCSV(size);
    }

    //zwraca dane w formacie csv z kolumnami podanymi kolumnami
    @Operation(summary = "convert to csv with given columns", description = "generates x=size random json object and converts them to csv format with given columns in 'columns' parameter")
    @GetMapping("/data/customCsv/{size}")
    public String convertToCSVWithGivenColumns(@PathVariable int size, @RequestParam String columns) {
        //tworzy listę kolumn podzielając ciąg znaków przecinkiem oraz ignorując wszystkie spacje
        Pattern pattern = Pattern.compile("\\s*,\\s*");
        List<String> columnList = Arrays.asList(pattern.split(columns));
        return dataProcessingService.convertToCSVWithGivenColumns(columnList, size);
    }

    //%2B dla + oraz %5E dla ^
    @Operation(summary = "Perform operations", description = "Generates x=size json objects with random data and perform mathematical operations on them. Operations are passed in 'operations' parameter in format 'operation1,operation2,operation3' etc. where operation is in format 'columnName1+columnName2' or 'columnName1^columnName2' etc.")
    @GetMapping("/calculate/{size}")
    public String performOperations(@PathVariable int size, @RequestParam String operations) {
        operations = operations.replaceAll("\\s", "");
        List<String> operationList = List.of(operations.split(","));
        return dataProcessingService.performOperations(operationList, size);
    }

}
