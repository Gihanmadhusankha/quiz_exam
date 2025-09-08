package com.quiz.quiz_exam.service;

import com.quiz.quiz_exam.dto.ExamMonitorDto;

public interface ExamMonitorService {
    ExamMonitorDto getExamMonitorData(Long teacherId ,Long examId);
    void endExam( Long teacherId,Long examId);
}
