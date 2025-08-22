package com.quiz.quiz_exam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamResultDto {
    private Long studentId;
    private Long examId;
    private int score;
    private boolean passed;
}
