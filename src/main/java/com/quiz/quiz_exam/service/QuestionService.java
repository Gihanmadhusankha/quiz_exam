package com.quiz.quiz_exam.service;


import com.quiz.quiz_exam.dto.QuestionDtos;
import com.quiz.quiz_exam.entity.Question;

import java.util.List;

public interface QuestionService {
    QuestionDtos.QuestionResponse addQuestion(QuestionDtos.CreateQuestionRequest req);
    void deleteQuestion(Long id);
    Question updateQuestion(Long id, QuestionDtos.CreateQuestionRequest req);
    List<QuestionDtos.QuestionResponse> listByExam(Long examId);
}
