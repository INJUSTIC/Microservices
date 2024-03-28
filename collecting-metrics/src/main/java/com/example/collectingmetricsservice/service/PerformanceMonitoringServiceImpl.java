package com.example.collectingmetricsservice.service;

import data_generation.client.ApiClient;
import data_generation.client.api.DataGenerationControllerApi;
import data_processing.client.api.DataProcessingControllerApi;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class PerformanceMonitoringServiceImpl implements PerformanceMonitoringService{

    private final DataGenerationControllerApi dataGenerationApi = new DataGenerationControllerApi(new ApiClient());
    private final DataProcessingControllerApi dataProcessingApi = new DataProcessingControllerApi(new data_processing.client.ApiClient());
    private final MeterRegistry meterRegistry;
    private final Map<Long, Double> usedMemoryByTime;
    //private final Map<Long, Double> usedCPUByTime;
    private long startTime, endTime;
    private ScheduledFuture<?> scheduledFuture;
    private ScheduledExecutorService executor;
    private StringBuilder finalReport;

    public PerformanceMonitoringServiceImpl() {
        this.meterRegistry = new SimpleMeterRegistry();
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        this.usedMemoryByTime = new LinkedHashMap<>();
        // Ustawienie metryki dla pamięci
        Gauge.builder("jvm.memory.used", memoryBean, bean -> bean.getHeapMemoryUsage().getUsed())
                .baseUnit("bytes")
                .register(meterRegistry);
    }

    @Override
    public String monitorPerformance() {
        //Usuwamy stare dane
        finalReport = new StringBuilder();
        //monitorowanie pamięci i tworzenie raportu dla size = 1000, 10 000 oraz 100 000 obiektów JSON
        finalReport.append("Performance report<br>");
        for (int size = 1000; size <= 100000; size*=10) {
            finalReport.append("<br><br><br>").append(size).append(" JSON objects <br><br><br>");
            finalReport.append("First service (3->1)<br><br>");
            monitorForGenerateJson(10);
            finalReport.append("<br><br> Second service (3->2->1)<br><br>");
            monitorForDataProcessingWithPredefinedColumns(size);
            monitorForDataProcessingWithGivenColumns(size);
            monitorForPerformOperations(size);
        }

        return finalReport.toString();
    }

    private void monitorForGenerateJson(int size) {
        finalReport.append("JSON generation endpoint <br><br>");
        try {
            startMonitoring();
            dataGenerationApi.generateJson(size);
            stopMonitoring();
            finalReport.append(generatePerformanceReport());
        } catch (data_generation.client.ApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void monitorForDataProcessingWithPredefinedColumns(int size) {
        finalReport.append("CSV generation with predefined columns <br><br>");
        try {
            startMonitoring();
            dataProcessingApi.convertToCSV(size);
            stopMonitoring();
            finalReport.append(generatePerformanceReport());
        } catch (data_processing.client.ApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void monitorForDataProcessingWithGivenColumns(int size) {
        finalReport.append("<br><br> CSV generation with given columns (for this example columns are: _type, key, name, latitude, longitude) <br><br>");
        try {
            startMonitoring();
            dataProcessingApi.convertToCSVWithGivenColumns(size, "_type,key,name,latitude,longitude");
            stopMonitoring();
            finalReport.append(generatePerformanceReport());
        } catch (data_processing.client.ApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void monitorForPerformOperations(int size) {
        finalReport.append("<br><br> Operation performer (for this example operations are: latitude*longitude, distance-latitude, sqrt(distance), latitude^2) <br><br>");
        try {
            startMonitoring();
            dataProcessingApi.performOperations(size, "latitude*longitude,distance-latitude,sqrt(distance),latitude^2");
            stopMonitoring();
            finalReport.append(generatePerformanceReport());
        } catch (data_processing.client.ApiException e) {
            throw new RuntimeException(e);
        }
    }

    //początek monitorowania zasobów
    private void startMonitoring() {
        //ustawianie funkcji która będzie się wywoływała po każdym update'u
        Runnable monitorMemoryAndCpu = () -> {
            long currTime = System.currentTimeMillis();
            long timeDiff = currTime - startTime;
            double usedMemory = meterRegistry.get("jvm.memory.used").gauge().value();
            //double usedCpu = meterRegistry.get("system.cpu.usage").gauge().value();
            usedMemoryByTime.put(timeDiff, usedMemory);
            //usedCPUByTime.put(timeDiff, usedCpu);
        };
        executor = Executors.newScheduledThreadPool(1);
        scheduledFuture = executor.scheduleAtFixedRate(
                monitorMemoryAndCpu,
                0,
                10, // update co 10 milisekund
                TimeUnit.MILLISECONDS
        );
        startTime = System.currentTimeMillis();
    }

    //skończenie monitorowania zasobów
    private void stopMonitoring() {
        endTime = System.currentTimeMillis();
        scheduledFuture.cancel(true);
        executor.shutdownNow();
        try {
            //czekamy dopóki nie skończy się
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    //generowanie raportu dla poszczególnego endpoint'u
    private String generatePerformanceReport() {
        StringBuilder report = new StringBuilder();
        report.append("Request time: ").append(endTime - startTime).append(" ms").append("<br><br>");
        report.append("Memory used<br>");
        for (long monitoredTime: usedMemoryByTime.keySet()) {
            double memoryUsed = (usedMemoryByTime.get(monitoredTime) / (1024.0 * 1024.0));
            String memoryUsedToShow = String.format("%.1f", memoryUsed);
            report.append(monitoredTime).append("ms: ").append(memoryUsedToShow).append(" MB").append("<br>");
        }
        /*report.append("<br><br>CPU used<br>");
        for (long monitoredTime: usedCPUByTime.keySet()) {
            double cpuUsed = usedCPUByTime.get(monitoredTime);
            report.append(monitoredTime).append("ms: ").append(cpuUsed).append("<br>");
        }*/
        return report.toString();
    }
}