package com.quiz.quiz_exam.dto;

import com.quiz.quiz_exam.enums.StudentExamStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
