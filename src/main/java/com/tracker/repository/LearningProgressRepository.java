package com.tracker.repository;

import com.tracker.model.LearningProgress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LearningProgressRepository extends JpaRepository<LearningProgress, Long> {
    
    /**
     * Find learning progress by student ID
     */
    List<LearningProgress> findByStudentId(Long studentId);
    
    /**
     * Find learning progress by student ID with pagination
     */
    Page<LearningProgress> findByStudentId(Long studentId, Pageable pageable);
    
    /**
     * Find learning progress by course ID
     */
    List<LearningProgress> findByCourseId(Long courseId);
    
    /**
     * Find learning progress by student and course
     */
    List<LearningProgress> findByStudentIdAndCourseId(Long studentId, Long courseId);
    
    /**
     * Find learning progress by date range
     */
    @Query("SELECT lp FROM LearningProgress lp WHERE lp.sessionDate BETWEEN :startDate AND :endDate ORDER BY lp.sessionDate DESC")
    List<LearningProgress> findBySessionDateBetween(@Param("startDate") LocalDate startDate, 
                                                   @Param("endDate") LocalDate endDate);
    
    /**
     * Find learning progress by student and date range
     */
    @Query("SELECT lp FROM LearningProgress lp WHERE lp.student.id = :studentId AND lp.sessionDate BETWEEN :startDate AND :endDate ORDER BY lp.sessionDate DESC")
    List<LearningProgress> findByStudentIdAndSessionDateBetween(@Param("studentId") Long studentId,
                                                               @Param("startDate") LocalDate startDate,
                                                               @Param("endDate") LocalDate endDate);
    
    /**
     * Get total hours studied by student
     */
    @Query("SELECT SUM(lp.hoursStudied) FROM LearningProgress lp WHERE lp.student.id = :studentId")
    Optional<BigDecimal> getTotalHoursStudiedByStudent(@Param("studentId") Long studentId);
    
    /**
     * Get total hours studied by student for a specific course
     */
    @Query("SELECT SUM(lp.hoursStudied) FROM LearningProgress lp WHERE lp.student.id = :studentId AND lp.course.id = :courseId")
    Optional<BigDecimal> getTotalHoursStudiedByStudentAndCourse(@Param("studentId") Long studentId, 
                                                               @Param("courseId") Long courseId);
    
    /**
     * Get average hours studied per session by student
     */
    @Query("SELECT AVG(lp.hoursStudied) FROM LearningProgress lp WHERE lp.student.id = :studentId")
    Optional<BigDecimal> getAverageHoursStudiedByStudent(@Param("studentId") Long studentId);
    
    /**
     * Get study sessions count by student
     */
    long countByStudentId(Long studentId);
    
    /**
     * Get study sessions count by student and course
     */
    long countByStudentIdAndCourseId(Long studentId, Long courseId);
    
    /**
     * Find recent learning progress by student
     */
    @Query("SELECT lp FROM LearningProgress lp WHERE lp.student.id = :studentId ORDER BY lp.sessionDate DESC, lp.createdAt DESC")
    List<LearningProgress> findRecentByStudent(@Param("studentId") Long studentId, Pageable pageable);
    
    /**
     * Find learning progress by difficulty level
     */
    List<LearningProgress> findByDifficultyLevel(Integer difficultyLevel);
    
    /**
     * Find learning progress by understanding level
     */
    List<LearningProgress> findByUnderstandingLevel(Integer understandingLevel);
    
    /**
     * Get study statistics by student
     */
    @Query("SELECT lp.student.id, lp.student.rollNumber, lp.student.user.name, " +
           "COUNT(lp.id) as totalSessions, " +
           "SUM(lp.hoursStudied) as totalHours, " +
           "AVG(lp.hoursStudied) as avgHoursPerSession, " +
           "AVG(lp.difficultyLevel) as avgDifficulty, " +
           "AVG(lp.understandingLevel) as avgUnderstanding " +
           "FROM LearningProgress lp " +
           "WHERE lp.student.id = :studentId " +
           "GROUP BY lp.student.id, lp.student.rollNumber, lp.student.user.name")
    Optional<Object[]> getStudyStatistics(@Param("studentId") Long studentId);
    
    /**
     * Get course study statistics
     */
    @Query("SELECT lp.course.id, lp.course.name, " +
           "COUNT(lp.id) as totalSessions, " +
           "SUM(lp.hoursStudied) as totalHours, " +
           "AVG(lp.hoursStudied) as avgHoursPerSession, " +
           "COUNT(DISTINCT lp.student.id) as uniqueStudents " +
           "FROM LearningProgress lp " +
           "WHERE lp.course.id = :courseId " +
           "GROUP BY lp.course.id, lp.course.name")
    Optional<Object[]> getCourseStudyStatistics(@Param("courseId") Long courseId);
    
    /**
     * Find most studied courses by student
     */
    @Query("SELECT lp.course, SUM(lp.hoursStudied) as totalHours " +
           "FROM LearningProgress lp " +
           "WHERE lp.student.id = :studentId " +
           "GROUP BY lp.course " +
           "ORDER BY totalHours DESC")
    List<Object[]> getMostStudiedCoursesByStudent(@Param("studentId") Long studentId, Pageable pageable);
    
    /**
     * Find learning progress with notes containing specific text
     */
    @Query("SELECT lp FROM LearningProgress lp WHERE LOWER(lp.notes) LIKE LOWER(CONCAT('%', :searchText, '%'))")
    List<LearningProgress> findByNotesContainingIgnoreCase(@Param("searchText") String searchText);
    
    /**
     * Find learning progress with topics containing specific text
     */
    @Query("SELECT lp FROM LearningProgress lp WHERE LOWER(lp.topicsCovered) LIKE LOWER(CONCAT('%', :searchText, '%'))")
    List<LearningProgress> findByTopicsCoveredContainingIgnoreCase(@Param("searchText") String searchText);
    
    /**
     * Get daily study hours for a student in date range
     */
    @Query("SELECT lp.sessionDate, SUM(lp.hoursStudied) " +
           "FROM LearningProgress lp " +
           "WHERE lp.student.id = :studentId AND lp.sessionDate BETWEEN :startDate AND :endDate " +
           "GROUP BY lp.sessionDate " +
           "ORDER BY lp.sessionDate")
    List<Object[]> getDailyStudyHours(@Param("studentId") Long studentId,
                                     @Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate);
    
    /**
     * Check if student has study session on specific date
     */
    boolean existsByStudentIdAndSessionDate(Long studentId, LocalDate sessionDate);
}