package com.quiz.quiz_exam.service;

import com.quiz.quiz_exam.dto.DashboardDto;

import java.util.List;
import java.util.Map;

public interface ExamDashboardService {
    DashboardDto.DashboardResponse getDashboard(Long teacherId);
}
