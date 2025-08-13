package com.quiz.quiz_exam.dto.requestDto;

import com.quiz.quiz_exam.entity.Question;
import com.quiz.quiz_exam.entity.User;
import com.quiz.quiz_exam.enums.ExamStatus;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExamRequestDto {
    private  String examId;
    private String title;
    private LocalDateTime examDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ExamStatus status;
}
