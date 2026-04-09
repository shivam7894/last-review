package com.tracker.service;

import com.tracker.repository.ResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ResultRepository resultRepository;

    public Map<String, Object> getPerformanceReport(Long studentId) {
        Map<String, Object> report = new HashMap<>();
        BigDecimal avgPercentage = resultRepository.getAveragePercentageByStudent(studentId).orElse(BigDecimal.ZERO);
        report.put("averagePercentage", avgPercentage);
        report.put("results", resultRepository.findByStudentId(studentId));
        return report;
    }
}
