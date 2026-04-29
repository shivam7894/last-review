package com.tracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "results", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "assessment_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"student", "assessment"})
public class Result {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    @NotNull(message = "Student is required")
    private Student student;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assessment_id", nullable = false)
    @NotNull(message = "Assessment is required")
    private Assessment assessment;
    
    @Column(name = "marks_obtained", nullable = false)
    @Min(value = 0, message = "Marks obtained cannot be negative")
    private Integer marksObtained;
    
    @Column(name = "total_marks", nullable = false)
    @Min(value = 1, message = "Total marks must be at least 1")
    private Integer totalMarks;
    
    // This will be calculated automatically by the database
    @Column(name = "percentage", precision = 5, scale = 2, insertable = false, updatable = false)
    private BigDecimal percentage;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "grade")
    private GradeType grade;
    
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum GradeType {
        A_PLUS("A+"), A("A"), B_PLUS("B+"), B("B"), 
        C_PLUS("C+"), C("C"), D("D"), F("F");
        
        private final String displayName;
        
        GradeType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        @Override
        public String toString() {
            return displayName;
        }
    }
    
    // Constructor for creating new results
    public Result(Student student, Assessment assessment, Integer marksObtained, Integer totalMarks) {
        this.student = student;
        this.assessment = assessment;
        this.marksObtained = marksObtained;
        this.totalMarks = totalMarks;
        this.submittedAt = LocalDateTime.now();
        this.grade = calculateGrade();
    }
    
    // Calculate grade based on percentage
    public GradeType calculateGrade() {
        if (totalMarks == null || totalMarks == 0 || marksObtained == null) {
            return GradeType.F;
        }
        
        double percentage = (marksObtained * 100.0) / totalMarks;
        
        if (percentage >= 90) return GradeType.A_PLUS;
        else if (percentage >= 80) return GradeType.A;
        else if (percentage >= 70) return GradeType.B_PLUS;
        else if (percentage >= 60) return GradeType.B;
        else if (percentage >= 50) return GradeType.C_PLUS;
        else if (percentage >= 40) return GradeType.C;
        else if (percentage >= 35) return GradeType.D;
        else return GradeType.F;
    }
    
    // Validation method
    @PrePersist
    @PreUpdate
    private void validateMarks() {
        if (marksObtained != null && totalMarks != null && marksObtained > totalMarks) {
            throw new IllegalArgumentException("Marks obtained cannot be greater than total marks");
        }
        // Auto-calculate grade
        this.grade = calculateGrade();
    }
}
