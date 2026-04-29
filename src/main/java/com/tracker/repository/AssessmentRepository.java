package com.tracker.repository;

import com.tracker.model.Assessment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    
    /**
     * Find assessments by course ID
     */
    List<Assessment> findByCourseId(Long courseId);
    
    /**
     * Find active assessments by course ID
     */
    List<Assessment> findByCourseIdAndIsActiveTrue(Long courseId);
    
    /**
     * Find assessments by type
     */
    List<Assessment> findByType(Assessment.AssessmentType type);
    
    /**
     * Find active assessments by type
     */
    List<Assessment> findByTypeAndIsActiveTrue(Assessment.AssessmentType type);
    
    /**
     * Find assessments by course and type
     */
    List<Assessment> findByCourseIdAndType(Long courseId, Assessment.AssessmentType type);
    
    /**
     * Find assessments by date range
     */
    @Query("SELECT a FROM Assessment a WHERE a.assessmentDate BETWEEN :startDate AND :endDate AND a.isActive = true")
    List<Assessment> findByAssessmentDateBetween(@Param("startDate") LocalDate startDate, 
                                               @Param("endDate") LocalDate endDate);
    
    /**
     * Find upcoming assessments
     */
    @Query("SELECT a FROM Assessment a WHERE a.assessmentDate >= :currentDate AND a.isActive = true ORDER BY a.assessmentDate")
    List<Assessment> findUpcomingAssessments(@Param("currentDate") LocalDate currentDate);
    
    /**
     * Find past assessments
     */
    @Query("SELECT a FROM Assessment a WHERE a.assessmentDate < :currentDate AND a.isActive = true ORDER BY a.assessmentDate DESC")
    List<Assessment> findPastAssessments(@Param("currentDate") LocalDate currentDate);
    
    /**
     * Find assessments for a student (through course enrollment)
     */
    @Query("SELECT a FROM Assessment a " +
           "JOIN a.course c " +
           "JOIN c.enrollments e " +
           "WHERE e.student.id = :studentId AND e.isActive = true AND a.isActive = true " +
           "ORDER BY a.assessmentDate DESC")
    List<Assessment> findAssessmentsForStudent(@Param("studentId") Long studentId);
    
    /**
     * Find assessments with results for a student
     */
    @Query("SELECT a FROM Assessment a " +
           "JOIN a.results r " +
           "WHERE r.student.id = :studentId " +
           "ORDER BY a.assessmentDate DESC")
    List<Assessment> findAssessmentsWithResultsForStudent(@Param("studentId") Long studentId);
    
    /**
     * Find assessments without results for a student
     */
    @Query("SELECT a FROM Assessment a " +
           "JOIN a.course c " +
           "JOIN c.enrollments e " +
           "WHERE e.student.id = :studentId AND e.isActive = true AND a.isActive = true " +
           "AND a.id NOT IN (SELECT r.assessment.id FROM Result r WHERE r.student.id = :studentId) " +
           "ORDER BY a.assessmentDate")
    List<Assessment> findAssessmentsWithoutResultsForStudent(@Param("studentId") Long studentId);
    
    /**
     * Count assessments by course
     */
    long countByCourseId(Long courseId);
    
    /**
     * Count active assessments by course
     */
    long countByCourseIdAndIsActiveTrue(Long courseId);
    
    /**
     * Count assessments by type
     */
    long countByType(Assessment.AssessmentType type);
    
    /**
     * Find assessments by title containing (case-insensitive)
     */
    @Query("SELECT a FROM Assessment a WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :title, '%')) AND a.isActive = true")
    List<Assessment> findByTitleContainingIgnoreCaseAndIsActiveTrue(@Param("title") String title);
    
    /**
     * Get assessment statistics by course
     */
    @Query("SELECT a.course.id, a.course.name, a.type, COUNT(a.id) as assessmentCount, AVG(a.totalMarks) as avgMarks " +
           "FROM Assessment a " +
           "WHERE a.isActive = true " +
           "GROUP BY a.course.id, a.course.name, a.type " +
           "ORDER BY a.course.name, a.type")
    List<Object[]> getAssessmentStatisticsByCourse();
    
    /**
     * Find all active assessments with pagination
     */
    Page<Assessment> findByIsActiveTrue(Pageable pageable);
    
    /**
     * Find assessments by course with pagination
     */
    Page<Assessment> findByCourseIdAndIsActiveTrue(Long courseId, Pageable pageable);
}