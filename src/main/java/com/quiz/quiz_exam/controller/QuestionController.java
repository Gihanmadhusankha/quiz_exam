package com.quiz.quiz_exam.controller;

import com.quiz.quiz_exam.dto.QuestionDtos;
import com.quiz.quiz_exam.service.QuestionService;
import com.quiz.quiz_exam.util.StandResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @PostMapping("/addQuestion")
    public ResponseEntity<StandResponseDto> add(@Valid @RequestBody QuestionDtos.CreateQuestionRequest req) {

        return new ResponseEntity<>(
                new StandResponseDto(
                        201, "Question Add successfully",questionService.addQuestion(req)

                ), HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/exam/{examId}")
    public ResponseEntity<StandResponseDto> listByExam(@PathVariable Long examId){
        return new ResponseEntity<>(
                new StandResponseDto(
                        200, "Questions",questionService.listByExam(examId)
                ), HttpStatus.OK
        );

    }
    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/update/{questionId}")
    public ResponseEntity<StandResponseDto>UpdateQuestion(@PathVariable Long questionId, @RequestBody QuestionDtos.CreateQuestionRequest request){
        return new ResponseEntity<>(
                new StandResponseDto(
                        201, "Question updated",questionService.updateQuestion(questionId,request)
                ), HttpStatus.CREATED
        );
    }
    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/delete/{questionId}")
    public ResponseEntity<StandResponseDto>deleteQuestion(@PathVariable Long questionId){
        questionService.deleteQuestion(questionId);
        return new ResponseEntity<>(
                new StandResponseDto(
                        204, "Question deleted",null
                ), HttpStatus.NO_CONTENT
        );
    }


}
