package com.tracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "learning_progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"student", "course"})
public class LearningProgress {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @NotNull(message = "Student is required")
    private Student student;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @NotNull(message = "Course is required")
    private Course course;
    
    @Column(name = "session_date", nullable = false)
    @NotNull(message = "Session date is required")
    private LocalDate sessionDate;
    
    @Column(name = "hours_studied", nullable = false, precision = 4, scale = 2)
    @NotNull(message = "Hours studied is required")
    @DecimalMin(value = "0.0", message = "Hours studied cannot be negative")
    @DecimalMax(value = "24.0", message = "Hours studied cannot exceed 24 hours per day")
    private BigDecimal hoursStudied;
    
    @Column(name = "topics_covered", columnDefinition = "TEXT")
    private String topicsCovered;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "difficulty_level")
    @Min(value = 1, message = "Difficulty level must be between 1 and 5")
    @Max(value = 5, message = "Difficulty level must be between 1 and 5")
    private Integer difficultyLevel;
    
    @Column(name = "understanding_level")
    @Min(value = 1, message = "Understanding level must be between 1 and 5")
    @Max(value = 5, message = "Understanding level must be between 1 and 5")
    private Integer understandingLevel;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructor for creating new learning progress entries
    public LearningProgress(Student student, Course course, LocalDate sessionDate, 
                           BigDecimal hoursStudied, String topicsCovered) {
        this.student = student;
        this.course = course;
        this.sessionDate = sessionDate;
        this.hoursStudied = hoursStudied;
        this.topicsCovered = topicsCovered;
    }
    
    // Convenience method to get difficulty level description
    public String getDifficultyDescription() {
        if (difficultyLevel == null) return "Not specified";
        return switch (difficultyLevel) {
            case 1 -> "Very Easy";
            case 2 -> "Easy";
            case 3 -> "Medium";
            case 4 -> "Hard";
            case 5 -> "Very Hard";
            default -> "Unknown";
        };
    }
    
    // Convenience method to get understanding level description
    public String getUnderstandingDescription() {
        if (understandingLevel == null) return "Not specified";
        return switch (understandingLevel) {
            case 1 -> "Poor";
            case 2 -> "Below Average";
            case 3 -> "Average";
            case 4 -> "Good";
            case 5 -> "Excellent";
            default -> "Unknown";
        };
    }
}