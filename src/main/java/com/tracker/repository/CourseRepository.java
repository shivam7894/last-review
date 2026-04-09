package com.tracker.repository;

import com.tracker.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    /**
     * Find course by code
     */
    Optional<Course> findByCode(String code);
    
    /**
     * Check if course code exists
     */
    boolean existsByCode(String code);
    
    /**
     * Find active courses
     */
    List<Course> findByIsActiveTrue();
    
    /**
     * Find active courses with pagination
     */
    Page<Course> findByIsActiveTrue(Pageable pageable);
    
    /**
     * Find courses by semester
     */
    List<Course> findBySemester(Integer semester);
    
    /**
     * Find active courses by semester
     */
    List<Course> findBySemesterAndIsActiveTrue(Integer semester);
    
    /**
     * Find courses by instructor name
     */
    @Query("SELECT c FROM Course c WHERE LOWER(c.instructorName) LIKE LOWER(CONCAT('%', :instructorName, '%'))")
    List<Course> findByInstructorNameContainingIgnoreCase(@Param("instructorName") String instructorName);
    
    /**
     * Search courses by name (case-insensitive)
     */
    @Query("SELECT c FROM Course c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) AND c.isActive = true")
    List<Course> findByNameContainingIgnoreCaseAndIsActiveTrue(@Param("name") String name);
    
    /**
     * Find courses enrolled by a specific student
     */
    @Query("SELECT c FROM Course c JOIN c.enrollments e WHERE e.student.id = :studentId AND e.isActive = true")
    List<Course> findCoursesEnrolledByStudent(@Param("studentId") Long studentId);
    
    /**
     * Find courses not enrolled by a specific student
     */
    @Query("SELECT c FROM Course c WHERE c.isActive = true AND c.id NOT IN " +
           "(SELECT e.course.id FROM StudentCourseEnrollment e WHERE e.student.id = :studentId AND e.isActive = true)")
    List<Course> findCoursesNotEnrolledByStudent(@Param("studentId") Long studentId);
    
    /**
     * Count active courses
     */
    long countByIsActiveTrue();
    
    /**
     * Count courses by semester
     */
    long countBySemester(Integer semester);
    
    /**
     * Get course enrollment statistics
     */
    @Query("SELECT c.id, c.code, c.name, COUNT(e.id) as enrollmentCount " +
           "FROM Course c LEFT JOIN c.enrollments e " +
           "WHERE c.isActive = true AND (e.isActive = true OR e.isActive IS NULL) " +
           "GROUP BY c.id, c.code, c.name " +
           "ORDER BY enrollmentCount DESC")
    List<Object[]> getCourseEnrollmentStatistics();
    
    /**
     * Find courses with assessments
     */
    @Query("SELECT DISTINCT c FROM Course c JOIN c.assessments a WHERE a.isActive = true AND c.isActive = true")
    List<Course> findCoursesWithAssessments();
    
    /**
     * Find courses by credits
     */
    List<Course> findByCredits(Integer credits);
    
    /**
     * Find courses ordered by name
     */
    @Query("SELECT c FROM Course c WHERE c.isActive = true ORDER BY c.name")
    List<Course> findAllActiveOrderByName();
}