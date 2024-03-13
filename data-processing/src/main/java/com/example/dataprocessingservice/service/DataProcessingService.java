package com.example.dataprocessingservice.service;
import java.util.List;
import com.example.dataprocessingservice.model.PersonData;

public interface DataProcessingService {
    String performOperations(List<String> operations, int size);

    String convertToCSVWithGivenColumns(List<String> columns, int size);

    String convertToCSV(int size);
    List<PersonData> getPersonDataListFromFirstService(int size);
}
