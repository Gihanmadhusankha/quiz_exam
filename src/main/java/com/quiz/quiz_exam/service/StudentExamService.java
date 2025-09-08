package com.quiz.quiz_exam.service;

import com.quiz.quiz_exam.dto.ResultDtos;
import com.quiz.quiz_exam.dto.StudentDtos;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentExamService {

    StudentDtos.AnswerDto submitAnswer(Long studentId,StudentDtos.SubmitAnswersRequest request);
    ResultDtos.StudentExamSummary finishExam(Long studentExamId,Long examId);
    ResultDtos.StudentResultRow getStudentResult(Long studentExamId);
    Page<StudentDtos.StudentExamList> StudentExamLists(Long studentId ,StudentDtos.StudentRequestExamList studentRequestExamList);
    StudentDtos.StartExamResponse startExam(Long examId, Long studentId);
}
