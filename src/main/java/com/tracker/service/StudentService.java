package com.tracker.service;

import com.tracker.dto.AdminStudentRequest;
import com.tracker.model.Student;
import com.tracker.model.User;
import com.tracker.repository.StudentRepository;
import com.tracker.repository.UserRepository;
import com.tracker.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student student) {
        Student existing = getStudentById(id);
        existing.setRollNumber(student.getRollNumber());
        existing.setDepartment(student.getDepartment());
        existing.setSemester(student.getSemester());
        return studentRepository.save(existing);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public Student createStudentFromAdminRequest(AdminStudentRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail().toLowerCase())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Create user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.UserRole.STUDENT);
        user = userRepository.save(user);
        
        // Create student
        Student student = new Student();
        student.setUser(user);
        student.setRollNumber(request.getRollNumber());
        student.setDepartment(request.getDepartment());
        student.setSemester(request.getSemester());
        return studentRepository.save(student);
    }
}
