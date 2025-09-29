package com.quiz.quiz_exam.service;

import com.quiz.quiz_exam.dto.DashboardDto;
import com.quiz.quiz_exam.dto.ExamDtos;
import com.quiz.quiz_exam.dto.StudentDtos;
import org.springframework.data.domain.Page;

public interface ExamService {

     ExamDtos.loadExamResponse loadExam(Long examId, Long teacherId) ;


    Page<ExamDtos.ExamResponse> listPublished(ExamDtos.TeacherExamList teacherExamList);
    ExamDtos.ExamResponse publish(Long examId);
    ExamDtos.ExamResponse saveUpdateDeleteExam(Long teacherId, ExamDtos.CreateExamRequest req );
    Page<ExamDtos.ExamResponse> listByTeacherExam(Long teacherId ,ExamDtos.TeacherExamList teacherExamList);



}

