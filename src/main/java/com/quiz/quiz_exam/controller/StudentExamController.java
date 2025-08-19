package com.quiz.quiz_exam.controller;

import com.quiz.quiz_exam.dto.StudentDtos.*;

import com.quiz.quiz_exam.service.StudentExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student-exams")
@RequiredArgsConstructor
public class StudentExamController {

    private final StudentExamService studentExamService;

    @PostMapping("/start")
    public ResponseEntity<StudentExamResponse> startExam(@RequestBody StartExamRequest request) {
        return ResponseEntity.ok(studentExamService.startExam(request));
    }

    @PostMapping("/submit")
    public ResponseEntity<StudentExamResponse> submitExam(@RequestBody SubmitAnswersRequest request) {
        return ResponseEntity.ok(studentExamService.submitExam(request));
    }
}
