package com.tracker.service;

import com.tracker.model.Assessment;
import com.tracker.model.Course;
import com.tracker.model.Result;
import com.tracker.model.Student;
import com.tracker.repository.AssessmentRepository;
import com.tracker.repository.CourseRepository;
import com.tracker.repository.LearningProgressRepository;
import com.tracker.repository.ResultRepository;
import com.tracker.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ResultRepository resultRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final AssessmentRepository assessmentRepository;
    private final LearningProgressRepository learningProgressRepository;

    public Map<String, Object> getOverviewReport() {
        List<Student> students = studentRepository.findAllWithUserDetails();
        List<Course> courses = courseRepository.findAll();
        List<Assessment> assessments = assessmentRepository.findAll();
        List<Result> results = resultRepository.findAll();

        Map<String, Object> report = new HashMap<>();
        report.put("totalStudents", students.size());
        report.put("totalCourses", courses.size());
        report.put("totalAssessments", assessments.size());
        report.put("totalResults", results.size());
        report.put("averagePerformance", averagePercentage(results));
        report.put("passRate", passRate(results));
        report.put("gradeDistribution", gradeDistribution(results));
        report.put("topPerformers", topPerformers());
        report.put("coursePerformance", coursePerformance(courses));
        report.put("outcomeCoverage", outcomeCoverage(assessments, results));
        report.put("atRiskStudents", atRiskStudents(students));
        return report;
    }

    public Map<String, Object> getPerformanceReport(Long studentId) {
        List<Result> results = resultRepository.findByStudentId(studentId);
        BigDecimal totalHours = learningProgressRepository.getTotalHoursStudiedByStudent(studentId).orElse(BigDecimal.ZERO);

        Map<String, Object> report = new HashMap<>();
        report.put("averagePercentage", averagePercentage(results));
        report.put("passRate", passRate(results));
        report.put("totalAssessments", results.size());
        report.put("totalHoursStudied", totalHours);
        report.put("results", results);
        report.put("gradeDistribution", gradeDistribution(results));
        report.put("coursePerformance", studentCoursePerformance(studentId));
        report.put("learningOutcomes", learningOutcomeStatus(results));
        report.put("strengths", buildStrengths(results));
        report.put("improvementAreas", buildImprovementAreas(results));
        return report;
    }

    public Map<String, Object> getPerformanceReportByEmail(String email) {
        Student student = studentRepository.findByUserEmail(email.toLowerCase())
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
        return getPerformanceReport(student.getId());
    }

    private BigDecimal averagePercentage(List<Result> results) {
        if (results == null || results.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = results.stream()
                .map(Result::getPercentage)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.divide(BigDecimal.valueOf(results.size()), 1, RoundingMode.HALF_UP);
    }

    private BigDecimal passRate(List<Result> results) {
        if (results == null || results.isEmpty()) {
            return BigDecimal.ZERO;
        }
        long passed = results.stream()
                .filter(result -> result.getPercentage() != null)
                .filter(result -> result.getPercentage().compareTo(BigDecimal.valueOf(40)) >= 0)
                .count();
        return BigDecimal.valueOf(passed * 100.0 / results.size()).setScale(1, RoundingMode.HALF_UP);
    }

    private Map<String, Long> gradeDistribution(List<Result> results) {
        Map<String, Long> distribution = new HashMap<>();
        for (Result result : results) {
            String grade = result.getGrade() != null ? result.getGrade().getDisplayName() : "N/A";
            distribution.put(grade, distribution.getOrDefault(grade, 0L) + 1);
        }
        return distribution;
    }

    private List<Map<String, Object>> topPerformers() {
        return resultRepository.getTopPerformers(PageRequest.of(0, 5)).stream()
                .map(row -> {
                    Student student = (Student) row[0];
                    BigDecimal average = toBigDecimal(row[1]);
                    Map<String, Object> item = new HashMap<>();
                    item.put("studentId", student.getId());
                    item.put("name", student.getName());
                    item.put("rollNumber", student.getRollNumber());
                    item.put("average", average.setScale(1, RoundingMode.HALF_UP));
                    item.put("grade", gradeFor(average));
                    return item;
                })
                .toList();
    }

    private List<Map<String, Object>> coursePerformance(List<Course> courses) {
        return courses.stream()
                .map(course -> {
                    List<Result> courseResults = resultRepository.findAll().stream()
                            .filter(result -> result.getAssessment() != null)
                            .filter(result -> result.getAssessment().getCourse() != null)
                            .filter(result -> Objects.equals(result.getAssessment().getCourse().getId(), course.getId()))
                            .toList();
                    Map<String, Object> item = new HashMap<>();
                    item.put("courseId", course.getId());
                    item.put("course", course.getName());
                    item.put("code", course.getCode());
                    item.put("average", averagePercentage(courseResults));
                    item.put("passRate", passRate(courseResults));
                    item.put("results", courseResults.size());
                    return item;
                })
                .toList();
    }

    private List<Map<String, Object>> studentCoursePerformance(Long studentId) {
        List<Result> results = resultRepository.findByStudentId(studentId);
        return results.stream()
                .filter(result -> result.getAssessment() != null && result.getAssessment().getCourse() != null)
                .map(result -> result.getAssessment().getCourse())
                .distinct()
                .map(course -> {
                    List<Result> courseResults = resultRepository.findByStudentIdAndCourseId(studentId, course.getId());
                    Map<String, Object> item = new HashMap<>();
                    item.put("courseId", course.getId());
                    item.put("course", course.getName());
                    item.put("code", course.getCode());
                    item.put("average", averagePercentage(courseResults));
                    item.put("grade", gradeFor(averagePercentage(courseResults)));
                    item.put("assessments", courseResults.size());
                    return item;
                })
                .toList();
    }

    private List<Map<String, Object>> learningOutcomeStatus(List<Result> results) {
        return results.stream()
                .filter(result -> result.getAssessment() != null)
                .map(result -> {
                    Assessment assessment = result.getAssessment();
                    Map<String, Object> item = new HashMap<>();
                    item.put("assessment", assessment.getTitle());
                    item.put("course", assessment.getCourse() != null ? assessment.getCourse().getName() : "");
                    item.put("outcome", assessment.getLearningOutcome() != null && !assessment.getLearningOutcome().isBlank()
                            ? assessment.getLearningOutcome()
                            : assessment.getTitle());
                    item.put("score", result.getPercentage());
                    item.put("status", result.getPercentage() != null && result.getPercentage().compareTo(BigDecimal.valueOf(60)) >= 0
                            ? "ACHIEVED"
                            : "NEEDS_IMPROVEMENT");
                    return item;
                })
                .toList();
    }

    private List<String> buildStrengths(List<Result> results) {
        List<String> strengths = results.stream()
                .filter(result -> result.getPercentage() != null && result.getPercentage().compareTo(BigDecimal.valueOf(75)) >= 0)
                .sorted(Comparator.comparing(Result::getPercentage).reversed())
                .limit(3)
                .map(result -> result.getAssessment() != null && result.getAssessment().getLearningOutcome() != null
                        ? result.getAssessment().getLearningOutcome()
                        : result.getAssessment() != null ? result.getAssessment().getTitle() : "Consistent scoring")
                .toList();
        return strengths.isEmpty() ? List.of("Complete more assessments to identify strengths") : strengths;
    }

    private List<String> buildImprovementAreas(List<Result> results) {
        List<String> areas = new ArrayList<>(results.stream()
                .filter(result -> result.getPercentage() != null && result.getPercentage().compareTo(BigDecimal.valueOf(60)) < 0)
                .sorted(Comparator.comparing(Result::getPercentage))
                .limit(4)
                .map(result -> {
                    if (result.getAssessment() != null && result.getAssessment().getLearningOutcome() != null) {
                        return result.getAssessment().getLearningOutcome();
                    }
                    if (result.getAssessment() != null) {
                        return result.getAssessment().getTitle();
                    }
                    return "Review assessment fundamentals";
                })
                .toList());
        if (areas.isEmpty()) {
            areas.add("Maintain practice consistency and review feedback after each assessment");
        }
        return areas;
    }

    private List<Map<String, Object>> outcomeCoverage(List<Assessment> assessments, List<Result> results) {
        return assessments.stream()
                .filter(assessment -> assessment.getLearningOutcome() != null && !assessment.getLearningOutcome().isBlank())
                .map(assessment -> {
                    List<Result> assessmentResults = results.stream()
                            .filter(result -> result.getAssessment() != null)
                            .filter(result -> Objects.equals(result.getAssessment().getId(), assessment.getId()))
                            .toList();
                    Map<String, Object> item = new HashMap<>();
                    item.put("outcome", assessment.getLearningOutcome());
                    item.put("assessment", assessment.getTitle());
                    item.put("course", assessment.getCourse() != null ? assessment.getCourse().getName() : "");
                    item.put("average", averagePercentage(assessmentResults));
                    item.put("studentsMeasured", assessmentResults.size());
                    return item;
                })
                .toList();
    }

    private List<Map<String, Object>> atRiskStudents(List<Student> students) {
        return students.stream()
                .map(student -> {
                    List<Result> results = resultRepository.findByStudentId(student.getId());
                    BigDecimal average = averagePercentage(results);
                    if (results.isEmpty() || average.compareTo(BigDecimal.valueOf(60)) >= 0) {
                        return null;
                    }
                    Map<String, Object> item = new HashMap<>();
                    item.put("studentId", student.getId());
                    item.put("name", student.getName());
                    item.put("rollNumber", student.getRollNumber());
                    item.put("average", average);
                    item.put("recommendation", "Schedule focused review and monitor next assessment");
                    return item;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    private String gradeFor(BigDecimal average) {
        if (average.compareTo(BigDecimal.valueOf(90)) >= 0) return "A+";
        if (average.compareTo(BigDecimal.valueOf(80)) >= 0) return "A";
        if (average.compareTo(BigDecimal.valueOf(70)) >= 0) return "B+";
        if (average.compareTo(BigDecimal.valueOf(60)) >= 0) return "B";
        if (average.compareTo(BigDecimal.valueOf(50)) >= 0) return "C+";
        if (average.compareTo(BigDecimal.valueOf(40)) >= 0) return "C";
        if (average.compareTo(BigDecimal.valueOf(35)) >= 0) return "D";
        return "F";
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value instanceof BigDecimal decimal) {
            return decimal;
        }
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        return BigDecimal.ZERO;
    }
}
