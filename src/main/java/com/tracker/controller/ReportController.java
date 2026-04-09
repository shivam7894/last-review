package com.tracker.controller;

import com.tracker.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    
    @GetMapping("/performance/{studentId}")
    public ResponseEntity<Map<String, Object>> getPerformanceReport(@PathVariable Long studentId) {
        return ResponseEntity.ok(reportService.getPerformanceReport(studentId));
    }
}
