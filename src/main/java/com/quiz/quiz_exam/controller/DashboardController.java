package com.quiz.quiz_exam.controller;

import com.quiz.quiz_exam.dto.DashboardDto;
import com.quiz.quiz_exam.dto.ExamDtos;
import com.quiz.quiz_exam.security.JwtUtil;
import com.quiz.quiz_exam.service.ExamDashboardService;
import com.quiz.quiz_exam.service.ExamService;
import com.quiz.quiz_exam.util.StandResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@PreAuthorize("hasRole('TEACHER')")
@RequiredArgsConstructor
public class DashboardController {

    private final ExamDashboardService examDashboardService;
    private final JwtUtil jwtUtil;

    @GetMapping("/progess")
    public ResponseEntity<StandResponseDto>getDashboard(
            @RequestHeader("Authorization")String authHeader) {
        String token=authHeader.substring(7);
        Long teacherId=jwtUtil.extractUserId(token);
        {

        return  new ResponseEntity<>(
                new StandResponseDto(
                        200, "Teacher Dashboard", examDashboardService.getDashboard(teacherId)
                ),
                HttpStatus.OK
        );

    }



    }









}
