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

    //  Teacher creates exam
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/create")
    public ResponseEntity<ExamDtos.ExamResponse> create(
            @RequestParam Long teacherId,
            @Valid @RequestBody ExamDtos.CreateExamRequest req) {
        return ResponseEntity.ok(examService.createExam(teacherId, req));
    }
    //Teacher update the exams
    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/update")
    public ResponseEntity<ExamDtos.ExamResponse> update(
            @RequestParam Long teacherId,
            @Valid @RequestBody ExamDtos.CreateExamRequest req) {
        return ResponseEntity.ok(examService.updateExam(teacherId, req));
    }

    //  Teacher lists own exams (Draft + Published)
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<ExamDtos.ExamResponse>> listByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(examService.listByTeacherExam(teacherId));
    }

    //  List published exams (for students)
    @GetMapping("/published")
    public ResponseEntity<List<ExamDtos.ExamResponse>> listPublished() {
        return ResponseEntity.ok(examService.listPublished());
    }

    //  Teacher publishes exam
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/{id}/publish")
    public ResponseEntity<ExamDtos.ExamResponse> publish(@PathVariable Long id) {
        return ResponseEntity.ok(examService.publish(id));
    }

    //  Get one exam (with questions)
    @GetMapping("/{id}")
    public ResponseEntity<ExamDtos.ExamResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(examService.get(id));
    }

    // Teacher delete the exam
    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        examService.deleteExam(id);
        return ResponseEntity.noContent().build();
    }
}
