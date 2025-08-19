package com.quiz.quiz_exam.service;

import com.quiz.quiz_exam.dto.ExamDtos;

import java.util.List;

public interface ExamService {
    ExamDtos.ExamResponse createExam(Long teacherId, ExamDtos.CreateExamRequest req);
    List<ExamDtos.ExamResponse> listPublished();
    ExamDtos.ExamResponse publish(Long examId);
    ExamDtos.ExamResponse get(Long id);
}
