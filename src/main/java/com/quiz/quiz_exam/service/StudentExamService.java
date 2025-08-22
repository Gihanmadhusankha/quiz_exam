package com.quiz.quiz_exam.service;

import com.quiz.quiz_exam.dto.ExamResultDto;
import com.quiz.quiz_exam.dto.ResultDtos;
import com.quiz.quiz_exam.dto.StudentDtos;
import org.springframework.data.domain.Page;

public interface StudentExamService {
    StudentDtos.StudentExamResponse startExam(StudentDtos.StartExamRequest request);
    StudentDtos.AnswerDto submitAnswer(StudentDtos.SubmitAnswersRequest request);
    ResultDtos.StudentExamSummary finishExam(Long studentExamId);
    ResultDtos.StudentResultRow getStudentResult(Long studentExamId);

}
