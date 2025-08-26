package com.quiz.quiz_exam.dto;

import com.quiz.quiz_exam.enums.ExamStatus;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

public class ExamDtos {
    public record QuestionDto(Long questionId, String questionText, String optionA, String optionB, String optionC, String optionD, String correctOption) { }
    public record CreateExamRequest(@NotBlank String title, @NotNull LocalDateTime date, @NotNull LocalDateTime startedTime, @NotNull LocalDateTime endTime, List<QuestionDto> questions) { }
    public record ExamResponse(Long examId, String title, LocalDateTime date, LocalDateTime startedTime, LocalDateTime endTime, ExamStatus status, List<QuestionDto> questions) { }
    public record TeacherExamList(
            long teacherId,
            int page,
            int size,
            String search
    ){}
}
