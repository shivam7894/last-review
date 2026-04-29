package com.tracker.service;

import com.tracker.dto.*;
import com.tracker.model.Student;
import com.tracker.model.User;
import com.tracker.repository.StudentRepository;
import com.tracker.repository.UserRepository;
import com.tracker.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final OtpService otpService;
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail().toLowerCase())) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        
        user = userRepository.save(user);
        
        // If registering as a student, create Student record
        if (request.getRole() == User.UserRole.STUDENT) {
            Student student = new Student(user, request.getRollNumber(), request.getDepartment(), request.getSemester());
            studentRepository.save(student);
        }
        
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
<<<<<<< HEAD
        Student student = request.getRole() == User.UserRole.STUDENT
                ? studentRepository.findByUserId(user.getId()).orElse(null)
                : null;
        return new AuthResponse(
                token,
                user.getEmail(),
                user.getName(),
                user.getRole().name(),
                student != null ? student.getId() : null,
                student != null ? student.getRollNumber() : null,
                student != null ? student.getDepartment() : null,
                student != null ? student.getSemester() : null
        );
=======
        return new AuthResponse(token, user.getEmail(), user.getName(), user.getRole().name());
>>>>>>> bcdac5e4088a6d85673b02eacfbaa20c07a73343
    }
    
    @Transactional
    public OtpAuthResponse login(LoginRequest request) {
        log.info("Login request received for email: {} with role: {}", request.getEmail(), request.getRole());
        
        // Auto-registration: Create user if not found
        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseGet(() -> {
                    log.info("User not found, auto-registering: {}", request.getEmail());
                    User newUser = new User();
                    newUser.setEmail(request.getEmail().toLowerCase());
                    newUser.setPassword(passwordEncoder.encode("OTP_USER_" + System.currentTimeMillis())); // Random password
                    newUser.setName(extractNameFromEmail(request.getEmail()));
                    
                    // Set role based on request, default to STUDENT
                    if (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN")) {
                        newUser.setRole(User.UserRole.ADMIN);
                    } else {
                        newUser.setRole(User.UserRole.STUDENT);
                    }
                    
                    User savedUser = userRepository.save(newUser);
                    log.info("✅ Auto-registered new user: {} with role: {}", savedUser.getEmail(), savedUser.getRole());
                    return savedUser;
                });
        
        if (request.getOtp() != null && !request.getOtp().isBlank()) {
            log.info("Verifying OTP for user: {}", user.getEmail());
            // Verify OTP
            if (otpService.verifyOtp(user.getEmail(), request.getOtp())) {
                log.info("OTP verified successfully for: {}", user.getEmail());
                String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
<<<<<<< HEAD
                Student student = ensureStudentProfile(user);
                return OtpAuthResponse.full(
                        token,
                        user.getEmail(),
                        user.getName(),
                        user.getRole().name(),
                        student != null ? student.getId() : null,
                        student != null ? student.getRollNumber() : null,
                        student != null ? student.getDepartment() : null,
                        student != null ? student.getSemester() : null
                );
=======
                return OtpAuthResponse.full(token, user.getEmail(), user.getName(), user.getRole().name());
>>>>>>> bcdac5e4088a6d85673b02eacfbaa20c07a73343
            } else {
                log.error("Invalid OTP for user: {}", user.getEmail());
                throw new RuntimeException("Invalid or expired OTP");
            }
        } else {
            log.info("Generating and sending OTP to: {}", user.getEmail());
            // Send OTP (no password needed)
            try {
<<<<<<< HEAD
                OtpService.OtpDelivery delivery = otpService.generateAndSendOtp(user.getEmail());
                log.info("OTP sent successfully to: {}", user.getEmail());
                return OtpAuthResponse.partial(
                        user.getEmail(),
                        user.getName(),
                        user.getRole().name(),
                        delivery.deliveryMethod(),
                        delivery.devOtp()
                );
=======
                otpService.generateAndSendOtp(user.getEmail());
                log.info("OTP sent successfully to: {}", user.getEmail());
                return OtpAuthResponse.partial(user.getEmail(), user.getName(), user.getRole().name());
>>>>>>> bcdac5e4088a6d85673b02eacfbaa20c07a73343
            } catch (Exception e) {
                log.error("Failed to send OTP to {}: {}", user.getEmail(), e.getMessage(), e);
                throw new RuntimeException("Failed to send OTP. Please check email configuration.");
            }
        }
    }
    
    // Helper method to extract name from email
    private String extractNameFromEmail(String email) {
        String localPart = email.split("@")[0];
        // Convert john.doe or john_doe to John Doe
        String name = localPart.replace(".", " ").replace("_", " ");
        // Capitalize first letter of each word
        String[] words = name.split(" ");
        StringBuilder capitalized = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                capitalized.append(Character.toUpperCase(word.charAt(0)))
                          .append(word.substring(1).toLowerCase())
                          .append(" ");
            }
        }
        return capitalized.toString().trim();
    }
<<<<<<< HEAD

    private Student ensureStudentProfile(User user) {
        if (user.getRole() != User.UserRole.STUDENT) {
            return null;
        }

        return studentRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Student student = new Student(user, null, null, null);
                    return studentRepository.save(student);
                });
    }
=======
>>>>>>> bcdac5e4088a6d85673b02eacfbaa20c07a73343
}
