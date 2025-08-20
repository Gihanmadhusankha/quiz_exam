package com.quiz.quiz_exam.controller;

import com.quiz.quiz_exam.dto.QuestionDtos;
import com.quiz.quiz_exam.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping
    public ResponseEntity<QuestionDtos.QuestionResponse> add(@Valid @RequestBody QuestionDtos.CreateQuestionRequest req) {
        return ResponseEntity.ok(questionService.addQuestion(req));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<QuestionDtos.QuestionResponse>> listByExam(@PathVariable Long examId) {
        return ResponseEntity.ok(questionService.listByExam(examId));
    }
}
