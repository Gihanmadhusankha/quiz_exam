package com.quiz.quiz_exam.dto;

import com.quiz.quiz_exam.enums.StudentExamStatus;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder

@Data
public class StudentDtos {

    public record StartExamRequest(
            @NotNull Long examId



    ) { }



    // Student’s answer for one question
    public record AnswerDto(
            Long examId,
            Long studentExamId,
            @NotNull Long questionId,
            @NotBlank String selectedOption


    ) { }

    // When submitting one answer → status = ATTENDED
    public record SubmitAnswersRequest(
            @NotNull Long examId,
            @NotNull Long questionId,
            @NotBlank String selectedOption
    ) { }


    public record StartExamResponse(
            Long examId,

            Long studentExamId,
            String title,

            LocalDateTime endTime,

            List<QuestionDtos.Question>questions,
            Long lastAnsweredQuestionId


    ){}
    public record StudentInfo(
            Long id,

            Long studentExamId,
            String name,
            String status


    ){}

    public record StudentStatusRecord(
            String studentName,
            String status

    ) {
    }
    public record StudentExamList(
            Long examId,

            Long studentExamId,
            String title,
            LocalDateTime StartTime,
            String ExamDuration,
            String status



    ){}
    public record StudentRequestExamList(

            int page,
            int size,
            String search
    ){}
}
