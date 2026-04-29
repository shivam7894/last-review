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
    private Long studentId;
    private String rollNumber;
    private String department;
    private Integer semester;
}
