package com.quiz.quiz_exam.service;

import com.quiz.quiz_exam.dto.requestDto.QuestionRequestDto;
import com.quiz.quiz_exam.dto.responseDto.QuestionResponseDto;

public interface QuestionService {
    QuestionResponseDto addQuestion(QuestionRequestDto questionRequestDto);
    QuestionResponseDto findQuestion(int questionId);
    void deleteQuestion(int questionId);
    void updateQuestion(int questionId, QuestionRequestDto questionRequestDto);
}
