package com.quiz.quiz_exam.controller;

import com.quiz.quiz_exam.dto.ExamDtos;
import com.quiz.quiz_exam.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {
    private final ExamService examService;


    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping
    public ResponseEntity<ExamDtos.ExamResponse> create(@RequestParam Long teacherId, @Valid @RequestBody ExamDtos.CreateExamRequest req) {
        return ResponseEntity.ok(examService.createExam(teacherId, req));
    }

    @GetMapping("/published")
    public ResponseEntity<List<ExamDtos.ExamResponse>> listPublished() {
        return ResponseEntity.ok(examService.listPublished());
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/<built-in function id>/publish")
    public ResponseEntity<ExamDtos.ExamResponse> publish(@PathVariable Long id) {
        return ResponseEntity.ok(examService.publish(id));
    }

    @GetMapping("/<built-in function id>")
    public ResponseEntity<ExamDtos.ExamResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(examService.get(id));
    }
}
