package com.quiz.quiz_exam.dto;

import com.quiz.quiz_exam.enums.StudentExamStatus;
import java.util.List;

public class ResultDtos {

    public record StudentExamSummary(
            Long studentExamId,
            Long examId,
            StudentExamStatus status,
            long totalQuestions,
            long correctCount,
            double percentage
    ) {}

    public record StudentResultRow(
            Long studentExamId,
            Long studentId,
            String studentName,
            StudentExamStatus status,
            long totalQuestions,
            long correctCount,
            double percentage
    ) {}

    public record ExamResultsForTeacher(
            Long examId,
            long attendedCount,
            long totalStudentExams,
            double averageScore,
            List<StudentResultRow> results
    ) {}

    public record MonitorCounts(
            Long examId,
            long totalStudentExams,
            long pendingCount,
            long attendedCount
    ) {}
}