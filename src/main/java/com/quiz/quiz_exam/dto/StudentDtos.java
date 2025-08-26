package com.quiz.quiz_exam.dto;

import com.quiz.quiz_exam.enums.ExamStatus;
import com.quiz.quiz_exam.enums.StudentExamStatus;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Builder

@Data
public class StudentDtos {



    // When student starts exam → status = PENDING
    public record StartExamRequest(
            @NotNull Long examId,
            @NotNull Long studentId,
            StudentExamStatus status
    ) { }

    // Student’s answer for one question
    public record AnswerDto(
            @NotNull Long questionId,
            @NotBlank String selectedOption


    ) { }

    // When submitting one answer → status = ATTENDED
    public record SubmitAnswersRequest(
            @NotNull Long studentExamId,
            @NotNull Long questionId,
            @NotBlank String selectedOption
    ) { }

    // Response after submission
    public record StudentExamResponse(
            Long studentExamId,
            Long examId,
            StudentExamStatus status,
            int totalQuestions,
            int correctCount,
            List<AnswerDto> answers

    ) {}
    public record StudentInfo(
            Long id,
            String name,
            String status


    ){}

    public record StudentStatusRecord(
            String studentName,
            String status //PENDING,ATTENDED,COMPLETED

    ) {
    }
    public record StudentExamList(
            String title,
            LocalDateTime StartTime,
            long ExamDuration,
            ExamStatus status



    ){}
    public record StudentRequestExamList(
            long studentId,
            int page,
            int size,
            String search
    ){}
}
