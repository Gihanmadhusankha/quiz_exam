package com.quiz.quiz_exam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class QuestionDtos {
    public record CreateQuestionRequest(
            @NotNull Long questionId,
            @NotBlank String questionText,
            @NotBlank String optionA,
            @NotBlank String optionB,
            @NotBlank String optionC,
            @NotBlank String optionD,
            @NotBlank String correctOption ,// only ONE correct answer

            boolean isNew,
            boolean isUpdate,
            boolean isRemove

    ) {}

    public record QuestionResponse(
            Long id,
         //   Long examId,
            String questionText,
            String optionA,
            String optionB,
            String optionC,
            String optionD,
            String correctOption   ) {}
    public record Question(
            Long id,
            String questionText,
            String optionA,
            String optionB,
            String optionC,
            String optionD
    ){}
}
