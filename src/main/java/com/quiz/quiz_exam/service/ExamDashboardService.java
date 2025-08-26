package com.quiz.quiz_exam.service;

import com.quiz.quiz_exam.dto.DashboardDto;

import java.util.List;
import java.util.Map;

public interface ExamDashboardService {
    List<DashboardDto.ProgressOverTime> getExamResultsOverTime(Long teacherId);
    List<DashboardDto.GradeDistribution>getGradeDistribution(Long teacherId);
    List<DashboardDto.studentAverage> getTopStudents(Long teacherId, int limit);
    List<DashboardDto.studentAverage> getLowStudents(Long teacherId, int limit);
}
