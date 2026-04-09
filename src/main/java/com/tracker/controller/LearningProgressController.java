package com.tracker.controller;

import com.tracker.model.LearningProgress;
import com.tracker.service.LearningProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/learning-progress")
@RequiredArgsConstructor
public class LearningProgressController {
    private final LearningProgressService learningProgressService;

    @GetMapping
    public ResponseEntity<List<LearningProgress>> getAllLearningProgress() {
        return ResponseEntity.ok(learningProgressService.getAllLearningProgress());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<LearningProgress>> getByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(learningProgressService.getByStudentId(studentId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LearningProgress> getById(@PathVariable Long id) {
        return ResponseEntity.ok(learningProgressService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or authentication.name == #learningProgress.student.user.email")
    public ResponseEntity<LearningProgress> createLearningProgress(@RequestBody LearningProgress learningProgress) {
        return ResponseEntity.ok(learningProgressService.create(learningProgress));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or authentication.name == #learningProgress.student.user.email")
    public ResponseEntity<LearningProgress> updateLearningProgress(@PathVariable Long id, @RequestBody LearningProgress learningProgress) {
        return ResponseEntity.ok(learningProgressService.update(id, learningProgress));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteLearningProgress(@PathVariable Long id) {
        learningProgressService.delete(id);
        return ResponseEntity.ok().build();
    }
}

