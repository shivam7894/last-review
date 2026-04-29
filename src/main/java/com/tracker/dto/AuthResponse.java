package com.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String email;
    private String name;
    private String role;
<<<<<<< HEAD
    private Long studentId;
    private String rollNumber;
    private String department;
    private Integer semester;
=======
>>>>>>> bcdac5e4088a6d85673b02eacfbaa20c07a73343
}
