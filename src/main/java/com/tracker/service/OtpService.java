package com.tracker.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

    // In-memory cache: email -> OTP, expires in 5 minutes
    private final Cache<String, String> otpCache = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

    private static final SecureRandom RANDOM = new SecureRandom();

    public void generateAndSendOtp(String email) {
        String otp = String.format("%06d", RANDOM.nextInt(1_000_000));
        otpCache.put(email.toLowerCase(), otp);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Bhai otp likh");
            message.setText("OTP likh bsdk: " + otp + "\n\nThis code expires in 5 minutes. Do not share it.");
            mailSender.send(message);
            log.info("✅ OTP sent to {} via email", email);
        } catch (Exception e) {
            // If email fails, log OTP to console for testing
            log.error("❌ Failed to send email to {}: {}", email, e.getMessage());
            log.warn("⚠️ EMAIL NOT CONFIGURED! Using console OTP for testing:");
            log.warn("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            log.warn("📧 Email: {}", email);
            log.warn("🔐 OTP: {}", otp);
            log.warn("⏰ Expires in 5 minutes");
            log.warn("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            log.warn("💡 Configure Gmail in application.properties to send real emails");
            log.warn("📖 See GMAIL_SETUP.md for instructions");
            // Don't throw exception - allow testing with console OTP
        }
    }

    public boolean verifyOtp(String email, String otp) {
        String cached = otpCache.getIfPresent(email.toLowerCase());
        if (cached != null && cached.equals(otp.trim())) {
            otpCache.invalidate(email.toLowerCase()); // one-time use
            log.info("✅ OTP verified successfully for {}", email);
            return true;
        }
        log.warn("❌ Invalid OTP for {}", email);
        return false;
    }
}
