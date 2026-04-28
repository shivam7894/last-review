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
    
    public static OtpAuthResponse full(String token, String email, String name, String role) {
        OtpAuthResponse res = new OtpAuthResponse();
        res.partial = false;
        res.token = token;
        res.email = email;
        res.name = name;
        res.role = role;
        return res;
    }
    
    public static OtpAuthResponse partial(String email, String name, String role) {
        OtpAuthResponse res = new OtpAuthResponse();
        res.partial = true;
        res.email = email;
        res.name = name;
        res.role = role;
        return res;
    }
}
