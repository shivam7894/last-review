package com.tracker.dto;

import com.tracker.model.User;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name cannot exceed 50 characters")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    private User.UserRole role = User.UserRole.STUDENT;
    
    // Student-specific fields (optional)
    private String rollNumber;
    private String department;
    private Integer semester;
}
