package com.quiz.quiz_exam.service;

import com.quiz.quiz_exam.dto.DashboardDto;
import com.quiz.quiz_exam.dto.ExamDtos;
import com.quiz.quiz_exam.dto.StudentDtos;
import org.springframework.data.domain.Page;

public interface ExamService {
    ExamDtos.ExamResponse createExam(Long teacherId, ExamDtos.CreateExamRequest req);
    Page<ExamDtos.ExamResponse> listPublished(int page, int size, String search);
    ExamDtos.ExamResponse publish(Long examId);
    ExamDtos.ExamResponse get(Long id);
    ExamDtos.ExamResponse updateExam(Long examId,ExamDtos.CreateExamRequest req);
    void deleteExam(Long examId);
    Page<ExamDtos.ExamResponse> listByTeacherExam(ExamDtos.TeacherExamList teacherExamList);




}

