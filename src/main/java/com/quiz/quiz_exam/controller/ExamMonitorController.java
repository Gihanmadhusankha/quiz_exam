package com.quiz.quiz_exam.controller;

import com.quiz.quiz_exam.dto.ExamMonitorDto;
import com.quiz.quiz_exam.service.ExamMonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exam/monitor")
@RequiredArgsConstructor
public class ExamMonitorController {
    private final ExamMonitorService examMonitorService;
    @GetMapping("/{examId}")
    public ResponseEntity<ExamMonitorDto> getExamMonitor(@PathVariable Long examId) {
        return ResponseEntity.ok(examMonitorService.getExamMonitorData(examId));
    }

    /*@PostMapping("/end/{examId}")
    public ResponseEntity<Void> endExam(@PathVariable Long examId) {
        examMonitorService.endExam(examId);
        return ResponseEntity.noContent().build();
    }*/
}
