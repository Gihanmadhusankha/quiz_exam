package com.quiz.quiz_exam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class QuestionDtos {
    public record CreateQuestionRequest(
            @NotNull Long examId,
            @NotBlank String questionText,
            @NotBlank String optionA,
            @NotBlank String optionB,
            @NotBlank String optionC,
            @NotBlank String optionD,
            @NotBlank String correctAnswer // only ONE correct answer
    ) {}

    public record QuestionResponse(
            Long id,
            Long examId,
            String questionText,
            String optionA,
            String optionB,
            String optionC,
            String optionD,
            String correctAnswer
    ) {}
}
