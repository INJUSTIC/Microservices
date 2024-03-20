package com.example.datagenerationservice.service;
import com.example.datagenerationservice.model.PersonData;
import java.util.List;

public interface DataGenerationService {
    List<PersonData> generateRandomPersonDataList(int size);
}
