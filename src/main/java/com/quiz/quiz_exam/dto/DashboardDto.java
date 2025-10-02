package com.quiz.quiz_exam.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter

@Builder
public class DashboardDto {

    public record ProgressOverTime(String examTitle, LocalDateTime examDate, double averageScore, Long attendedCount) {
    }


    public record GradeDistribution(String grade, Long count, double percent) {
    }

    public record studentAverage(Long examId, String studentName, Double averageScore) {

    }

    public record DashboardResponse(List<ProgressOverTime> progressOverTime, List<GradeDistribution> gradeDistribution,
                                    List<studentAverage> topStudents, List<studentAverage> lowStudents) {}
}
