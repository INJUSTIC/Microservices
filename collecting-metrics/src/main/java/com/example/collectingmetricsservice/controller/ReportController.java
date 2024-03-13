package com.example.collectingmetricsservice.controller;

import com.example.collectingmetricsservice.service.PerformanceMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

    PerformanceMonitoringService performanceMonitoringService;

    @Autowired
    public ReportController(PerformanceMonitoringService performanceMonitoringService) {
        this.performanceMonitoringService = performanceMonitoringService;
    }

    @GetMapping("/statistics")
    public String generateStatistics() {
        return performanceMonitoringService.monitorPerformance();
    }
}
