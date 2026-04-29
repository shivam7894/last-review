package com.tracker.controller;

import com.tracker.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

<<<<<<< HEAD
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
=======
@CrossOrigin(origins = "http://localhost:5173")
>>>>>>> bcdac5e4088a6d85673b02eacfbaa20c07a73343
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
<<<<<<< HEAD

    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getOverviewReport() {
        return ResponseEntity.ok(reportService.getOverviewReport());
    }
=======
>>>>>>> bcdac5e4088a6d85673b02eacfbaa20c07a73343
    
    @GetMapping("/performance/{studentId}")
    public ResponseEntity<Map<String, Object>> getPerformanceReport(@PathVariable Long studentId) {
        return ResponseEntity.ok(reportService.getPerformanceReport(studentId));
    }
<<<<<<< HEAD

    @GetMapping("/performance/email/{email}")
    public ResponseEntity<Map<String, Object>> getPerformanceReportByEmail(@PathVariable String email) {
        return ResponseEntity.ok(reportService.getPerformanceReportByEmail(email));
    }
=======
>>>>>>> bcdac5e4088a6d85673b02eacfbaa20c07a73343
}
