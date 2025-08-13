package com.quiz.quiz_exam.service;

import com.quiz.quiz_exam.dto.requestDto.ExamRequestDto;
import com.quiz.quiz_exam.dto.responseDto.ExamResponseDto;
import com.quiz.quiz_exam.dto.responseDto.paginate.ExamPaginateResponseDto;

public interface ExamService {
    ExamResponseDto addExam(ExamRequestDto examRequestDto);
    ExamResponseDto updateExam(String examId , ExamRequestDto examRequestDto);


    ExamPaginateResponseDto findAll(int page, int size, String searchText);

     void deleteExam(String examId);


}
