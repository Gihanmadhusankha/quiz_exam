package com.quiz.quiz_exam.controller;

import com.quiz.quiz_exam.dto.DashboardDto;
import com.quiz.quiz_exam.service.ExamDashboardService;
import com.quiz.quiz_exam.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@PreAuthorize("hasRole('TEACHER')")
@RequiredArgsConstructor
public class DashboardController {

    private final ExamDashboardService examDashboardService;

    @GetMapping("/progess/{teacherId}")
    public ResponseEntity<?>getProgress(@PathVariable Long teacherId){
        return ResponseEntity.ok(examDashboardService.getExamResultsOverTime(teacherId));

    }

    @GetMapping("/grades/{teacherId}")
    public ResponseEntity<?>getGradeDistribution(@PathVariable Long teacherId){
        return ResponseEntity.ok(examDashboardService.getGradeDistribution(teacherId));

    }
    @GetMapping("/low-students/{teacherId}")
    public ResponseEntity<?>getLowStudents(@PathVariable Long teacherId,@RequestParam(defaultValue = "5") int limit){
        return ResponseEntity.ok(examDashboardService.getLowStudents(teacherId,limit));

    }
    @GetMapping("/top-students/{teacherId}")
    public ResponseEntity<?>getTopStudents(@PathVariable Long teacherId,@RequestParam(defaultValue = "5") int limit){
        return ResponseEntity.ok(examDashboardService.getTopStudents(teacherId,limit));

    }









}
