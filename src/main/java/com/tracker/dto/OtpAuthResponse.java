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
<<<<<<< HEAD
    private Long studentId;
    private String rollNumber;
    private String department;
    private Integer semester;
    private String deliveryMethod;
    private String devOtp;
    
    public static OtpAuthResponse full(String token, String email, String name, String role, Long studentId,
                                       String rollNumber, String department, Integer semester) {
=======
    
    public static OtpAuthResponse full(String token, String email, String name, String role) {
>>>>>>> bcdac5e4088a6d85673b02eacfbaa20c07a73343
        OtpAuthResponse res = new OtpAuthResponse();
        res.partial = false;
        res.token = token;
        res.email = email;
        res.name = name;
        res.role = role;
<<<<<<< HEAD
        res.studentId = studentId;
        res.rollNumber = rollNumber;
        res.department = department;
        res.semester = semester;
        return res;
    }
    
    public static OtpAuthResponse partial(String email, String name, String role, String deliveryMethod, String devOtp) {
=======
        return res;
    }
    
    public static OtpAuthResponse partial(String email, String name, String role) {
>>>>>>> bcdac5e4088a6d85673b02eacfbaa20c07a73343
        OtpAuthResponse res = new OtpAuthResponse();
        res.partial = true;
        res.email = email;
        res.name = name;
        res.role = role;
<<<<<<< HEAD
        res.deliveryMethod = deliveryMethod;
        res.devOtp = devOtp;
=======
>>>>>>> bcdac5e4088a6d85673b02eacfbaa20c07a73343
        return res;
    }
}
