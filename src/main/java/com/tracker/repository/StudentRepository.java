package com.tracker.repository;

import com.tracker.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    /**
     * Find student by roll number
     */
    Optional<Student> findByRollNumber(String rollNumber);
    
    /**
     * Find student by user ID
     */
    Optional<Student> findByUserId(Long userId);
    
    /**
     * Find student by user email
     */
    @Query("SELECT s FROM Student s JOIN s.user u WHERE u.email = :email")
    Optional<Student> findByUserEmail(@Param("email") String email);
    
    /**
     * Check if roll number exists
     */
    boolean existsByRollNumber(String rollNumber);
    
    /**
     * Find students by department
     */
    List<Student> findByDepartment(String department);
    
    /**
     * Find students by department with pagination
     */
    Page<Student> findByDepartment(String department, Pageable pageable);
    
    /**
     * Find students by semester
     */
    List<Student> findBySemester(Integer semester);
    
    /**
     * Find students by department and semester
     */
    List<Student> findByDepartmentAndSemester(String department, Integer semester);
    
    /**
     * Search students by name (case-insensitive)
     */
    @Query("SELECT s FROM Student s JOIN s.user u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Student> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Get all students with their user details
     */
    @Query("SELECT s FROM Student s JOIN FETCH s.user ORDER BY s.user.name")
    List<Student> findAllWithUserDetails();
    
    /**
     * Count students by department
     */
    long countByDepartment(String department);
    
    /**
     * Count students by semester
     */
    long countBySemester(Integer semester);
    
    /**
     * Find students enrolled in a specific course
     */
    @Query("SELECT DISTINCT s FROM Student s JOIN s.enrollments e WHERE e.course.id = :courseId AND e.isActive = true")
    List<Student> findStudentsEnrolledInCourse(@Param("courseId") Long courseId);
    
    /**
     * Find students with results in a specific assessment
     */
    @Query("SELECT DISTINCT s FROM Student s JOIN s.results r WHERE r.assessment.id = :assessmentId")
    List<Student> findStudentsWithResultsInAssessment(@Param("assessmentId") Long assessmentId);
    
    /**
     * Get student performance summary
     */
    @Query("SELECT s.id, s.rollNumber, u.name, " +
           "COUNT(r.id) as totalAssessments, " +
           "AVG(r.percentage) as averagePercentage " +
           "FROM Student s " +
           "JOIN s.user u " +
           "LEFT JOIN s.results r " +
           "GROUP BY s.id, s.rollNumber, u.name " +
           "ORDER BY averagePercentage DESC")
    List<Object[]> getStudentPerformanceSummary();
    
    /**
     * Find top performing students
     */
    @Query("SELECT s FROM Student s " +
           "JOIN s.results r " +
           "GROUP BY s " +
           "ORDER BY AVG(r.percentage) DESC")
    List<Student> findTopPerformingStudents(Pageable pageable);
}