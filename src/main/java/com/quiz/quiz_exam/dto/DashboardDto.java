package com.quiz.quiz_exam.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter

@Builder
public class DashboardDto {

    public record ProgressOverTime(
            String examTitle,
            LocalDateTime examDate,
            double averageScore,
            Long attendedCount
    ) {}


    public record GradeDistribution(
            String grade,   // A, B, C, D, F
            Long count,
            double percent
    ) {}

    public record studentAverage(
            Long examId,
            String studentName,
            Double averageScore
    ) {

    }


}
