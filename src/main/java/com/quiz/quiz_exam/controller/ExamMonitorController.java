package com.quiz.quiz_exam.controller;

import com.quiz.quiz_exam.dto.ExamMonitorDto;
import com.quiz.quiz_exam.service.ExamMonitorService;
import com.quiz.quiz_exam.util.StandResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exam/monitor")
@RequiredArgsConstructor
public class ExamMonitorController {
    private final ExamMonitorService examMonitorService;
    @GetMapping("/{examId}")
    public ResponseEntity<StandResponseDto> getExamMonitor(@PathVariable Long examId) {
        return  new ResponseEntity<>(
                new StandResponseDto(
                        200, "Exam Data",examMonitorService.getExamMonitorData(examId)
                ), HttpStatus.OK
        );
    }
    @PostMapping("/end/{examId}")
    public ResponseEntity<StandResponseDto> endExam(@PathVariable Long examId){
        examMonitorService.endExam(examId);

        return  new ResponseEntity<>(
                new StandResponseDto(
                        200, "Exam ended successfully",null
                ), HttpStatus.OK
        );
    }


}
