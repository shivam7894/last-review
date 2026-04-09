package com.tracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assessments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"course", "results"})
public class Assessment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @NotNull(message = "Course is required")
    private Course course;
    
    @Column(name = "title", nullable = false)
    @NotBlank(message = "Assessment title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @NotNull(message = "Assessment type is required")
    private AssessmentType type;
    
    @Column(name = "total_marks", nullable = false)
    @Min(value = 1, message = "Total marks must be at least 1")
    private Integer totalMarks;
    
    @Column(name = "passing_marks", nullable = false)
    @Min(value = 0, message = "Passing marks cannot be negative")
    private Integer passingMarks;
    
    @Column(name = "assessment_date")
    private LocalDate assessmentDate;
    
    @Column(name = "duration_minutes")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationMinutes;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relationships
    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Result> results = new ArrayList<>();
    
    public enum AssessmentType {
        QUIZ, ASSIGNMENT, EXAM, PROJECT
    }
    
    // Constructor for creating new assessments
    public Assessment(Course course, String title, String description, AssessmentType type, 
                     Integer totalMarks, Integer passingMarks) {
        this.course = course;
        this.title = title;
        this.description = description;
        this.type = type;
        this.totalMarks = totalMarks;
        this.passingMarks = passingMarks;
    }
    
    // Validation method
    @PrePersist
    @PreUpdate
    private void validateMarks() {
        if (passingMarks != null && totalMarks != null && passingMarks > totalMarks) {
            throw new IllegalArgumentException("Passing marks cannot be greater than total marks");
        }
    }
}