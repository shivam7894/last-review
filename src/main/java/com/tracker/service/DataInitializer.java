package com.tracker.service;

import com.tracker.model.Student;
import com.tracker.model.User;
import com.tracker.repository.StudentRepository;
import com.tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("Loading initial data...");

            // Admin user - password: admin123
            User admin = new User();
            admin.setEmail("admin@test.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setName("Admin User");
            admin.setRole(User.UserRole.ADMIN);
            userRepository.save(admin);

            // Student user - password: student123
            User studentUser = new User();
            studentUser.setEmail("student@test.com");
            studentUser.setPassword(passwordEncoder.encode("student123"));
            studentUser.setName("John Doe");
            studentUser.setRole(User.UserRole.STUDENT);
            userRepository.save(studentUser);

            // Student record
            Student student = new Student(studentUser, "CSE2021001", "Computer Science", 6);
            studentRepository.save(student);

            log.info("✅ Initial data loaded: admin@test.com/admin123, student@test.com/student123");
        } else {
            log.info("Database already has data, skipping initialization");
        }
    }
}
