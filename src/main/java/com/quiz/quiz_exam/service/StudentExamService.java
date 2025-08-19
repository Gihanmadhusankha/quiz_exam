package com.quiz.quiz_exam.service;

import com.quiz.quiz_exam.dto.StudentDtos;

public interface StudentExamService {
    StudentDtos.StudentExamResponse startExam(StudentDtos.StartExamRequest request);
    StudentDtos.StudentExamResponse submitExam(StudentDtos.SubmitAnswersRequest request);
}
