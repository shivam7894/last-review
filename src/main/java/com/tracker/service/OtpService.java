package com.tracker.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {

    private final JavaMailSender mailSender;

    private final Cache<String, String> otpCache = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

    private static final SecureRandom RANDOM = new SecureRandom();

    public OtpDelivery generateAndSendOtp(String email) {
        String normalizedEmail = email.toLowerCase();
        String otp = String.format("%06d", RANDOM.nextInt(1_000_000));
        otpCache.put(normalizedEmail, otp);

        if (normalizedEmail.endsWith("@test.com")) {
            log.info("Using development OTP for demo account {}", normalizedEmail);
            return new OtpDelivery(false, "DEV_CONSOLE", otp);
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(normalizedEmail);
            message.setSubject("Your EduTrack OTP");
            message.setText("Your EduTrack verification code is: " + otp
                    + "\n\nThis code expires in 5 minutes. Do not share it.");
            mailSender.send(message);
            log.info("OTP sent to {} via email", normalizedEmail);
            return new OtpDelivery(true, "EMAIL", null);
        } catch (Exception e) {
            log.error("Failed to send OTP email to {}: {}", normalizedEmail, e.getMessage());
            log.warn("Development OTP for {} is {}", normalizedEmail, otp);
            return new OtpDelivery(false, "DEV_CONSOLE", otp);
        }
    }

    public boolean verifyOtp(String email, String otp) {
        String normalizedEmail = email.toLowerCase();
        String cached = otpCache.getIfPresent(normalizedEmail);
        if (cached != null && cached.equals(otp.trim())) {
            otpCache.invalidate(normalizedEmail);
            log.info("OTP verified successfully for {}", normalizedEmail);
            return true;
        }

        log.warn("Invalid OTP for {}", normalizedEmail);
        return false;
    }

    public record OtpDelivery(boolean emailSent, String deliveryMethod, String devOtp) {
    }
}
