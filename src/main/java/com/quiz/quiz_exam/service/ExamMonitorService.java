package com.quiz.quiz_exam.service;

import com.quiz.quiz_exam.dto.ExamMonitorDto;

public interface ExamMonitorService {
    ExamMonitorDto getExamMonitorData(Long examId);
   // void endExam(Long examId);
}
