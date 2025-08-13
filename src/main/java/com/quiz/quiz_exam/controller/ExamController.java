package com.quiz.quiz_exam.controller;

import com.quiz.quiz_exam.dto.requestDto.ExamRequestDto;
import com.quiz.quiz_exam.dto.requestDto.UserRequestDto;
import com.quiz.quiz_exam.dto.responseDto.ExamResponseDto;
import com.quiz.quiz_exam.dto.responseDto.UserResponseDto;
import com.quiz.quiz_exam.service.ExamService;
import com.quiz.quiz_exam.util.StandardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/exams")
@RequiredArgsConstructor
public class ExamController {
    private final ExamService examService;

    @PostMapping
    public ResponseEntity<StandardResponseDto> createExam(@RequestBody ExamRequestDto examRequestDto) {
        ExamResponseDto createdExam = examService.addExam(examRequestDto);
        return new ResponseEntity<>(
                new StandardResponseDto(201, "User created", createdExam),
                HttpStatus.CREATED
        );
    }
    @DeleteMapping("/{examId}")
    public ResponseEntity<StandardResponseDto> deleteExam(@PathVariable String examId){
        examService.deleteExam(examId);
        return ResponseEntity.ok(new StandardResponseDto(204,"Exam deleted successfully",null));
    }
    @PutMapping("/{examId}")
    public ResponseEntity<StandardResponseDto> updateExam(
            @PathVariable String examId,
            @RequestBody ExamRequestDto examRequestDto
    ) {
        ExamResponseDto updatedExam = examService.updateExam(examId, examRequestDto);
        return ResponseEntity.ok(new StandardResponseDto(200, "Exam updated successfully", updatedExam));
    }

    @GetMapping("/search")
    public ResponseEntity<StandardResponseDto> searchUsers(
            @RequestParam String searchText,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ResponseEntity.ok(
                new StandardResponseDto(200, "User data", examService.findAll(page, size, searchText))
        );
    }



}




