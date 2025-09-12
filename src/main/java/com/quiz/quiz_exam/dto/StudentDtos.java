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
            Long studentExamId,
            String title,
          //  String duration ,
            List<QuestionDtos.Question>questions


    ){

    }
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
            String status



    ){}
    public record StudentRequestExamList(

            int page,
            int size,
            String search
    ){}
}
