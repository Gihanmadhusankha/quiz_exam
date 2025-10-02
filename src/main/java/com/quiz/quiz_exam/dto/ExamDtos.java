package com.quiz.quiz_exam.dto;

import com.quiz.quiz_exam.enums.ExamStatus;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

public class ExamDtos {
    public record QuestionDto(Long questionId, String questionText, String optionA, String optionB, String optionC, String optionD, String correctOption) { }
    public record CreateExamRequest(@NotBlank String title,  LocalDateTime date,  LocalDateTime startedTime,  LocalDateTime endTime, List<QuestionDtos.CreateQuestionRequest> questions,Long examId, boolean isNew,boolean isUpdate,boolean isRemove) { }
    public record ExamResponse(long examId,String title ,LocalDateTime lastUpdated,ExamStatus status) { }
    public record TeacherExamList(int page, int size, String search){}
    public record Request(Long examId, boolean isTimerEnd){}
    public record loadExamRequest(@NotNull Long examId, boolean isTimerEnd){}
    public record loadExamResponse(Long examId, String title, LocalDateTime date, LocalDateTime startedTime, LocalDateTime endTime, List<QuestionDto> questions){}

}
