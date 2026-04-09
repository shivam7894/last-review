package com.tracker.repository;

import com.tracker.model.StudentCourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentCourseEnrollmentRepository extends JpaRepository<StudentCourseEnrollment, Long> {
    
    /**
     * Find enrollments by student ID
     */
    List<StudentCourseEnrollment> findByStudentId(Long studentId);
    
    /**
     * Find active enrollments by student ID
     */
    List<StudentCourseEnrollment> findByStudentIdAndIsActiveTrue(Long studentId);
    
    /**
     * Find enrollments by course ID
     */
    List<StudentCourseEnrollment> findByCourseId(Long courseId);
    
    /**
     * Find active enrollments by course ID
     */
    List<StudentCourseEnrollment> findByCourseIdAndIsActiveTrue(Long courseId);
    
    /**
     * Find specific enrollment by student and course
     */
    Optional<StudentCourseEnrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);
    
    /**
     * Find active enrollment by student and course
     */
    Optional<StudentCourseEnrollment> findByStudentIdAndCourseIdAndIsActiveTrue(Long studentId, Long courseId);
    
    /**
     * Check if student is enrolled in course
     */
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    
    /**
     * Check if student is actively enrolled in course
     */
    boolean existsByStudentIdAndCourseIdAndIsActiveTrue(Long studentId, Long courseId);
    
    /**
     * Find enrollments by date range
     */
    @Query("SELECT e FROM StudentCourseEnrollment e WHERE e.enrollmentDate BETWEEN :startDate AND :endDate")
    List<StudentCourseEnrollment> findByEnrollmentDateBetween(@Param("startDate") LocalDate startDate, 
                                                             @Param("endDate") LocalDate endDate);
    
    /**
     * Count active enrollments by student
     */
    long countByStudentIdAndIsActiveTrue(Long studentId);
    
    /**
     * Count active enrollments by course
     */
    long countByCourseIdAndIsActiveTrue(Long courseId);
    
    /**
     * Get enrollment statistics by course
     */
    @Query("SELECT e.course.id, e.course.code, e.course.name, COUNT(e.id) as enrollmentCount " +
           "FROM StudentCourseEnrollment e " +
           "WHERE e.isActive = true " +
           "GROUP BY e.course.id, e.course.code, e.course.name " +
           "ORDER BY enrollmentCount DESC")
    List<Object[]> getEnrollmentStatisticsByCourse();
    
    /**
     * Get enrollment statistics by student
     */
    @Query("SELECT e.student.id, e.student.rollNumber, e.student.user.name, COUNT(e.id) as enrollmentCount " +
           "FROM StudentCourseEnrollment e " +
           "WHERE e.isActive = true " +
           "GROUP BY e.student.id, e.student.rollNumber, e.student.user.name " +
           "ORDER BY enrollmentCount DESC")
    List<Object[]> getEnrollmentStatisticsByStudent();
    
    /**
     * Find recent enrollments
     */
    @Query("SELECT e FROM StudentCourseEnrollment e WHERE e.isActive = true ORDER BY e.enrollmentDate DESC, e.createdAt DESC")
    List<StudentCourseEnrollment> findRecentEnrollments(@Param("limit") int limit);
    
    /**
     * Find enrollments by semester (through course)
     */
    @Query("SELECT e FROM StudentCourseEnrollment e JOIN e.course c WHERE c.semester = :semester AND e.isActive = true")
    List<StudentCourseEnrollment> findBySemester(@Param("semester") Integer semester);
    
    /**
     * Find student's enrollments by semester
     */
    @Query("SELECT e FROM StudentCourseEnrollment e JOIN e.course c WHERE e.student.id = :studentId AND c.semester = :semester AND e.isActive = true")
    List<StudentCourseEnrollment> findByStudentIdAndSemester(@Param("studentId") Long studentId, 
                                                            @Param("semester") Integer semester);
}