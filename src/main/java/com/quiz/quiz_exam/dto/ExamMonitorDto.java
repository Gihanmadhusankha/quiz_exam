package com.quiz.quiz_exam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamMonitorDto {
    private String examName;
    private LocalDateTime examStartTime;
    private LocalDateTime examEndTime;
    private Long completedCount;
    private Long totalCount;
    private String remainingTime;
    private List<StudentDtos.StudentInfo> Students;
}
