package com.quiz.quiz_exam.service;

import com.quiz.quiz_exam.dto.StudentDtos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentExamService {
    StudentDtos.StudentExamResponse startExam(StudentDtos.StartExamRequest request);
    StudentDtos.StudentExamResponse submitExam(StudentDtos.SubmitAnswersRequest request);
    StudentDtos.StudentExamResponse getStudentExam(Long studentExamId);

    Page<StudentDtos.StudentExamResponse> listStudentExams(Long studentId, int page, int size, String search);
}
