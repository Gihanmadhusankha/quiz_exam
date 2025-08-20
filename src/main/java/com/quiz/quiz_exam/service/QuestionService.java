package com.quiz.quiz_exam.service;


import com.quiz.quiz_exam.dto.QuestionDtos;

import java.util.List;

public interface QuestionService {
    QuestionDtos.QuestionResponse addQuestion(QuestionDtos.CreateQuestionRequest req);
    List<QuestionDtos.QuestionResponse> listByExam(Long examId);
}
