package com.quiz.quiz_exam.controller;

import com.quiz.quiz_exam.dto.ExamDtos;
import com.quiz.quiz_exam.dto.ExamMonitorDto;
import com.quiz.quiz_exam.security.JwtUtil;
import com.quiz.quiz_exam.service.ExamMonitorService;
import com.quiz.quiz_exam.util.StandResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exam/monitor")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class ExamMonitorController {
    private final ExamMonitorService examMonitorService;
    private final JwtUtil jwtUtil;
    @PostMapping
    public ResponseEntity<StandResponseDto> getExamMonitor(@RequestHeader("Authorization")String authHeader, @RequestBody ExamDtos.Request req) {
        String token=authHeader.substring(7);
        Long teacherId=jwtUtil.extractUserId(token);
        return  new ResponseEntity<>(
                new StandResponseDto(
                        200, "Exam Data",examMonitorService.getExamMonitorData(teacherId, req.examId())
                ), HttpStatus.OK
        );
    }
    @PostMapping("/end")
    public ResponseEntity<StandResponseDto> endExam(@RequestHeader("Authorization")String authHeader,@RequestBody ExamDtos.Request req){
        String token=authHeader.substring(7);
        Long teacherId=jwtUtil.extractUserId(token);

        examMonitorService.endExam(teacherId, req);

        return  new ResponseEntity<>(
                new StandResponseDto(
                        200, "Exam ended successfully",null
                ), HttpStatus.OK
        );
    }


}
