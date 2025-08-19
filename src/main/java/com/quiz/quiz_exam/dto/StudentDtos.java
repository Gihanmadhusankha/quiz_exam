package com.quiz.quiz_exam.dto;

import com.quiz.quiz_exam.enums.StudentExamStatus;
import jakarta.validation.constraints.*;
import java.util.List;

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

    // When submitting all answers → status = ATTENDED
    public record SubmitAnswersRequest(
            @NotNull Long studentExamId,
            List<AnswerDto> answers
    ) { }

    // Response after submission
    public record StudentExamResponse(
            Long studentExamId,
            Long examId,
            StudentExamStatus status,
            int totalQuestions,
            int correctCount,
            List<AnswerDto> answers
    ) { }
}
