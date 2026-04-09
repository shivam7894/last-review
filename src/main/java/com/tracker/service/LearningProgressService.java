package com.tracker.service;

import com.tracker.model.LearningProgress;
import com.tracker.repository.LearningProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LearningProgressService {
    private final LearningProgressRepository learningProgressRepository;

    public List<LearningProgress> getAllLearningProgress() {
        return learningProgressRepository.findAll();
    }

    public LearningProgress getById(Long id) {
        return learningProgressRepository.findById(id).orElseThrow(() -> new RuntimeException("LearningProgress not found"));
    }

    public LearningProgress create(LearningProgress learningProgress) {
        return learningProgressRepository.save(learningProgress);
    }

    public LearningProgress update(Long id, LearningProgress learningProgress) {
        learningProgress.setId(id);
        return learningProgressRepository.save(learningProgress);
    }

    public void delete(Long id) {
        learningProgressRepository.deleteById(id);
    }

    public List<LearningProgress> getByStudentId(Long studentId) {
        return learningProgressRepository.findByStudentId(studentId);
    }
}

