package com.quiz.quiz_exam.dto.responseDto;

import com.quiz.quiz_exam.enums.ExamStatus;
import lombok.*;

import java.sql.Date;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExamResponseDto {
    private String examId;
    private String title;
    private LocalDateTime examDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ExamStatus status;
}
