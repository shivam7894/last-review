package com.tracker.dto;

import lombok.Data;

@Data
public class OtpAuthResponse {
    private boolean partial;
    private String email;
    private String name;
    private String role;
    // token only if !partial
    private String token;
    private Long studentId;
    private String rollNumber;
    private String department;
    private Integer semester;
    private String deliveryMethod;
    private String devOtp;
    
    public static OtpAuthResponse full(String token, String email, String name, String role, Long studentId,
                                       String rollNumber, String department, Integer semester) {
        OtpAuthResponse res = new OtpAuthResponse();
        res.partial = false;
        res.token = token;
        res.email = email;
        res.name = name;
        res.role = role;
        res.studentId = studentId;
        res.rollNumber = rollNumber;
        res.department = department;
        res.semester = semester;
        return res;
    }
    
    public static OtpAuthResponse partial(String email, String name, String role, String deliveryMethod, String devOtp) {
        OtpAuthResponse res = new OtpAuthResponse();
        res.partial = true;
        res.email = email;
        res.name = name;
        res.role = role;
        res.deliveryMethod = deliveryMethod;
        res.devOtp = devOtp;
        return res;
    }
}
