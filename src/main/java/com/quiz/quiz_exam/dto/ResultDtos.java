package com.quiz.quiz_exam.dto;

import com.quiz.quiz_exam.enums.StudentExamStatus;
import java.util.List;

public class ResultDtos {
    public record ResultRequest(
            Long studentExamId
    ){}

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
            long correctAnswers,
            double percentage,
            int obtainedPoints,
            int totalPoints,
            String grade ,
            String passFail,
            List<QuestionResult>question
    ) {}

    public record ExamResultsForTeacher(
            Long examId,
            long attendedCount,
            long totalStudentExams,
            double averageScore,
            List<StudentResultRow> results
    ) {}


    public record QuestionResult(
            Long questionId,
            String tittle,
            boolean correct,
            String verdictText
    ){}
    public record ResultResponse(
            String statusText,
            String grade,
            int points,
            int totalPoints,
            List<QuestionResult>questions
    ){}
}