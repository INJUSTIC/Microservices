package com.example.dataprocessingservice.service;

import com.example.dataprocessingservice.model.PersonData;
import com.example.dataprocessingservice.operationPerformer.OperationPerformer;
import org.springframework.stereotype.Service;
import io.swagger.client.*;
import io.swagger.client.api.DataGenerationControllerApi;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataProcessingServiceImpl implements DataProcessingService {

    @Override
    public String performOperations(List<String> operations, int size) {
        List<PersonData> personDataList = getPersonDataListFromFirstService(size);
        return OperationPerformer.performOperations(personDataList, operations);
    }

    @Override
    public String convertToCSVWithGivenColumns(List<String> columns, int size) {
        List<PersonData> personDataList = getPersonDataListFromFirstService(size);
        StringBuilder csvData = new StringBuilder();

        // Nagłówki
        String headers = String.join(",", columns) + "<br>";
        csvData.append(headers);

        for (PersonData personData : personDataList) {
            StringBuilder rowData = new StringBuilder();

            //szukamy pola które mają taką samą nazwę jak kolumne
            for (String column : columns) {
                Object colValue = getColValue(personData, column);
                rowData.append(colValue).append(";");
            }

            // Usuwanie ostatniego przecinku w wierszu
            rowData.deleteCharAt(rowData.length() - 1);
            csvData.append(rowData).append("<br>");
        }

        return csvData.toString();
    }

    @Override
    public String convertToCSV(int size) {
        List<PersonData> personDataList = getPersonDataListFromFirstService(size);
        String headers = "Type, _id, Name, Type, Latitude, Longitude <br>";

        // Tworzenie danych w formacie CSV
        StringBuilder csvData = new StringBuilder();
        for (PersonData data : personDataList) {
            csvData.append(String.format("%s; %d; %s; %s; %f; %f <br>",
                    data.get_type(), data.get_id(), data.getName(), data.getType(),
                    data.getGeoPosition().getLatitude(), data.getGeoPosition().getLongitude()));
        }

        return headers + csvData;
    }

    private Object getColValue(PersonData personData, String column) {
        return switch(column) {
            case "_id" -> personData.get_id();
            case "_type" -> personData.get_type();
            case "key" -> personData.getKey();
            case "name" -> personData.getName();
            case "fullName" -> personData.getFullName();
            case "location_id" -> personData.getLocation_id();
            case "iata_airport_code" -> personData.getIata_airport_code();
            case "type" -> personData.getType();
            case "coreCountry" -> personData.getCountryData();
            case "distance" -> personData.getDistanceInKm();
            case "geo_position" -> personData.getGeoPosition();
            case "latitude" -> personData.getGeoPosition().getLatitude();
            case "longitude" -> personData.getGeoPosition().getLongitude();
            case "country" -> personData.getCountryData().getCountry();
            case "inEurope" -> personData.getCountryData().isInEurope();
            case "countryCode" -> personData.getCountryData().getCountryCode();
            //w przypadku niepoprawnej kolumny
            default -> throw new IllegalArgumentException("Incorrect column: " + column);
        };
    }

    @Override
    public List<PersonData> getPersonDataListFromFirstService(int size) {
        DataGenerationControllerApi apiInstance = new DataGenerationControllerApi();
        try {
            // Call the generateJson method to retrieve data from the Data Generation Service
            List<io.swagger.client.model.PersonData> result = apiInstance.generateJson(size);

            // Convert the List of generated PersonData to your Data Processing Service's model

            return getPersonData(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DataGenerationControllerApi#generateJson");
            e.printStackTrace();
            // Handle exception appropriately
            return null;
        }
    }

    private static List<PersonData> getPersonData(List<io.swagger.client.model.PersonData> result) {
        List<PersonData> processedResult = new ArrayList<>();
        for (io.swagger.client.model.PersonData data : result) {
            // Convert each item from the generated model to your Data Processing Service's model
            PersonData processedData = new PersonData();
            // Set properties of processedData based on the corresponding properties in data
            processedData.setName(data.getName());
            processedData.setKey(data.getKey());
            processedData.setFullName(data.getFullName());
            processedData.setLocation_id(data.getLocationId());
            processedData.setIata_airport_code(data.getIataAirportCode());
            processedData.setType(data.getType());
            processedData.setCoreCountry(data.isCoreCountry());
            processedData.setDistanceInKm(data.getDistanceInKm());
            processedData.setCountryData(new com.example.dataprocessingservice.model.CountryData(data.getCountryData().getCountry(), data.getCountryData().isInEurope(), data.getCountryData().getCountryCode(), data.getCountryData().getMinLatitude(), data.getCountryData().getMaxLatitude(), data.getCountryData().getMinLongitude(), data.getCountryData().getMaxLongitude()));
            processedData.setGeoPosition(new com.example.dataprocessingservice.model.GeoPosition(data.getGeoPosition().getLatitude(), data.getGeoPosition().getLongitude()));
            // Map other properties as needed
            processedResult.add(processedData);
        }
        return processedResult;
    }

}
