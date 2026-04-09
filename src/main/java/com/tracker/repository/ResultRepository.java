package com.tracker.repository;

import com.tracker.model.Assessment;
import com.tracker.model.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    
    /**
     * Find results by student ID
     */
    List<Result> findByStudentId(Long studentId);
    
    /**
     * Find results by student ID with pagination
     */
    Page<Result> findByStudentId(Long studentId, Pageable pageable);
    
    /**
     * Find results by assessment ID
     */
    List<Result> findByAssessmentId(Long assessmentId);
    
    /**
     * Find result by student and assessment (should be unique)
     */
    Optional<Result> findByStudentIdAndAssessmentId(Long studentId, Long assessmentId);
    
    /**
     * Find results by grade
     */
    List<Result> findByGrade(Result.GradeType grade);
    
    /**
     * Find results by student and course (through assessment)
     */
    @Query("SELECT r FROM Result r JOIN r.assessment a WHERE r.student.id = :studentId AND a.course.id = :courseId")
    List<Result> findByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
    
    /**
     * Find results with percentage above threshold
     */
    @Query("SELECT r FROM Result r WHERE r.percentage >= :threshold ORDER BY r.percentage DESC")
    List<Result> findResultsAbovePercentage(@Param("threshold") BigDecimal threshold);
    
    /**
     * Find results with percentage below threshold
     */
    @Query("SELECT r FROM Result r WHERE r.percentage < :threshold ORDER BY r.percentage ASC")
    List<Result> findResultsBelowPercentage(@Param("threshold") BigDecimal threshold);
    
    /**
     * Get student's average percentage
     */
    @Query("SELECT AVG(r.percentage) FROM Result r WHERE r.student.id = :studentId")
    Optional<BigDecimal> getAveragePercentageByStudent(@Param("studentId") Long studentId);
    
    /**
     * Get course average percentage
     */
    @Query("SELECT AVG(r.percentage) FROM Result r JOIN r.assessment a WHERE a.course.id = :courseId")
    Optional<BigDecimal> getAveragePercentageByCourse(@Param("courseId") Long courseId);
    
    /**
     * Get assessment average percentage
     */
    @Query("SELECT AVG(r.percentage) FROM Result r WHERE r.assessment.id = :assessmentId")
    Optional<BigDecimal> getAveragePercentageByAssessment(@Param("assessmentId") Long assessmentId);
    
    /**
     * Count results by grade
     */
    long countByGrade(Result.GradeType grade);
    
    /**
     * Count passing results (grade != F)
     */
    @Query("SELECT COUNT(r) FROM Result r WHERE r.grade != 'F'")
    long countPassingResults();
    
    /**
     * Count failing results (grade = F)
     */
    @Query("SELECT COUNT(r) FROM Result r WHERE r.grade = 'F'")
    long countFailingResults();
    
    /**
     * Get grade distribution
     */
    @Query("SELECT r.grade, COUNT(r) FROM Result r GROUP BY r.grade ORDER BY r.grade")
    List<Object[]> getGradeDistribution();
    
    /**
     * Get grade distribution by course
     */
    @Query("SELECT r.grade, COUNT(r) FROM Result r JOIN r.assessment a WHERE a.course.id = :courseId GROUP BY r.grade ORDER BY r.grade")
    List<Object[]> getGradeDistributionByCourse(@Param("courseId") Long courseId);
    
    /**
     * Get top performers (highest average percentage)
     */
    @Query("SELECT r.student, AVG(r.percentage) as avgPercentage " +
           "FROM Result r " +
           "GROUP BY r.student " +
           "ORDER BY avgPercentage DESC")
    List<Object[]> getTopPerformers(Pageable pageable);
    
    /**
     * Get student performance summary
     */
    @Query("SELECT r.student.id, r.student.rollNumber, r.student.user.name, " +
           "COUNT(r.id) as totalAssessments, " +
           "AVG(r.percentage) as averagePercentage, " +
           "MAX(r.percentage) as maxPercentage, " +
           "MIN(r.percentage) as minPercentage " +
           "FROM Result r " +
           "WHERE r.student.id = :studentId " +
           "GROUP BY r.student.id, r.student.rollNumber, r.student.user.name")
    Optional<Object[]> getStudentPerformanceSummary(@Param("studentId") Long studentId);
    
    /**
     * Get course performance summary
     */
    @Query("SELECT a.course.id, a.course.name, " +
           "COUNT(r.id) as totalResults, " +
           "AVG(r.percentage) as averagePercentage, " +
           "COUNT(CASE WHEN r.grade != 'F' THEN 1 END) as passCount, " +
           "COUNT(CASE WHEN r.grade = 'F' THEN 1 END) as failCount " +
           "FROM Result r JOIN r.assessment a " +
           "WHERE a.course.id = :courseId " +
           "GROUP BY a.course.id, a.course.name")
    Optional<Object[]> getCoursePerformanceSummary(@Param("courseId") Long courseId);
    
    /**
     * Find recent results for a student
     */
    @Query("SELECT r FROM Result r WHERE r.student.id = :studentId ORDER BY r.createdAt DESC")
    List<Result> findRecentResultsByStudent(@Param("studentId") Long studentId, Pageable pageable);
    
    /**
     * Find results by assessment type
     */
    @Query("SELECT r FROM Result r JOIN r.assessment a WHERE a.type = :assessmentType")
    List<Result> findByAssessmentType(@Param("assessmentType") Assessment.AssessmentType assessmentType);
    
    /**
     * Check if student has result for assessment
     */
    boolean existsByStudentIdAndAssessmentId(Long studentId, Long assessmentId);
    
    /**
     * Get total hours studied by student (from learning progress)
     */
    @Query("SELECT SUM(lp.hoursStudied) FROM LearningProgress lp WHERE lp.student.id = :studentId")
    Optional<BigDecimal> getTotalHoursStudiedByStudent(@Param("studentId") Long studentId);
}