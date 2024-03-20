package com.example.datagenerationservice.repository;
import com.example.datagenerationservice.model.PersonData;
import java.util.List;

public interface DataGenerationRepository {
    List<PersonData> generateRandomPersonDataList(int size);
}
