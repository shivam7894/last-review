package com.tracker.repository;

import com.tracker.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by email address
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if user exists by email
     */
    boolean existsByEmail(String email);
    
    /**
     * Find users by role
     */
    List<User> findByRole(User.UserRole role);
    
    /**
     * Find users by role with pagination
     */
    Page<User> findByRole(User.UserRole role, Pageable pageable);
    
    /**
     * Find users by name containing (case-insensitive search)
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Count users by role
     */
    long countByRole(User.UserRole role);
    
    /**
     * Find users created after a specific date
     */
    @Query("SELECT u FROM User u WHERE u.createdAt >= :date ORDER BY u.createdAt DESC")
    List<User> findUsersCreatedAfter(@Param("date") java.time.LocalDateTime date);
    
    /**
     * Find active students (users with STUDENT role)
     */
    @Query("SELECT u FROM User u WHERE u.role = 'STUDENT' ORDER BY u.name")
    List<User> findAllStudentUsers();
    
    /**
     * Find admin users
     */
    @Query("SELECT u FROM User u WHERE u.role = 'ADMIN' ORDER BY u.name")
    List<User> findAllAdminUsers();
}