package com.tracker.controller;

import com.tracker.dto.AdminStudentRequest;
import com.tracker.model.Student;
import com.tracker.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"}, allowCredentials = "true")
public class StudentController {
    private final StudentService studentService;
    
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents(Authentication authentication) {
        log.info("Getting all students - User: {}, Authorities: {}", 
                authentication.getName(), authentication.getAuthorities());
        List<Student> students = studentService.getAllStudents();
        log.info("Retrieved {} students", students.size());
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id, Authentication authentication) {
        log.info("Getting student by ID: {} - User: {}", id, authentication.getName());
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }
    
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Student> createStudent(@RequestBody AdminStudentRequest request, Authentication authentication) {
        log.info("Admin creating student - User: {}, Student email: {}", authentication.getName(), request.getEmail());
        Student createdStudent = studentService.createStudentFromAdminRequest(request);
        log.info("Student created with ID: {}", createdStudent.getId());
        return ResponseEntity.ok(createdStudent);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student, Authentication authentication) {
        log.info("Creating student - User: {}", authentication.getName());
        Student createdStudent = studentService.createStudent(student);
        log.info("Student created with ID: {}", createdStudent.getId());
        return ResponseEntity.ok(createdStudent);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @Valid @RequestBody Student student, Authentication authentication) {
        log.info("Updating student ID: {} - User: {}", id, authentication.getName());
        Student updatedStudent = studentService.updateStudent(id, student);
        return ResponseEntity.ok(updatedStudent);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id, Authentication authentication) {
        log.info("Deleting student ID: {} - User: {}", id, authentication.getName());
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }
}
