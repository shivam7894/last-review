package com.tracker.controller;

import com.tracker.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getOverviewReport() {
        return ResponseEntity.ok(reportService.getOverviewReport());
    }
    
    @GetMapping("/performance/{studentId}")
    public ResponseEntity<Map<String, Object>> getPerformanceReport(@PathVariable Long studentId) {
        return ResponseEntity.ok(reportService.getPerformanceReport(studentId));
    }

    @GetMapping("/performance/email/{email}")
    public ResponseEntity<Map<String, Object>> getPerformanceReportByEmail(@PathVariable String email) {
        return ResponseEntity.ok(reportService.getPerformanceReportByEmail(email));
    }
}
