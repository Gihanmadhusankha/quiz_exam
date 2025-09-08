package com.quiz.quiz_exam.service.Impl;

import com.quiz.quiz_exam.dto.ExamMonitorDto;
import com.quiz.quiz_exam.dto.StudentDtos;
import com.quiz.quiz_exam.entity.Exam;
import com.quiz.quiz_exam.entity.StudentExam;
import com.quiz.quiz_exam.enums.ExamStatus;
import com.quiz.quiz_exam.exception.EntryNotfoundException;
import com.quiz.quiz_exam.repository.ExamRepository;
import com.quiz.quiz_exam.repository.StudentExamRepository;
import com.quiz.quiz_exam.service.ExamMonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ExamMonitorServiceImpl implements ExamMonitorService {
    private final ExamRepository examRepository;
    private final StudentExamRepository studentExamRepository;

    @Override
    public ExamMonitorDto getExamMonitorData(Long teacherId,Long examId) {
        Exam exam =examRepository.findById(examId)
                .orElseThrow(()-> new EntryNotfoundException("Exam not found"));
        long completeCount=studentExamRepository.countCompleted(examId);
        List<StudentExam> studentExams = studentExamRepository.findAllByExamId(examId);

        List<StudentDtos.StudentStatusRecord> students = studentExams.stream()
                .map(se -> new StudentDtos.StudentStatusRecord(se.getStudent().getName(), se.getStatus().toString()))
                .toList();

        long totalCount = studentExams.size();

        Duration remaining = Duration.between(LocalDateTime.now(), exam.getEndTime());
        if (remaining.isNegative()) {
            remaining = Duration.ZERO;
        }
        long minutes = remaining.toMinutes();
        long seconds = remaining.minusMinutes(minutes).getSeconds();
        String formatted = String.format("%02d:%02d", minutes, seconds);

        ExamMonitorDto dto = new ExamMonitorDto();
        dto.setExamName(exam.getTitle());
        dto.setExamStartTime(exam.getStartTime());
        dto.setExamEndTime(exam.getEndTime());
        dto.setCompletedCount(completeCount);
        dto.setTotalCount(totalCount);
        dto.setRemainingTime(formatted);
        dto.setStudents(studentExams.stream()
                .map(se -> new StudentDtos.StudentInfo(
                        se.getStudent().getUserId(),
                        se.getStudent().getName(),
                        se.getStudentExamStatus().toString()
                )).toList()
        );


        return dto;
    }

    @Override
    public void endExam( Long teacherId,Long examId) {

            Exam exam=examRepository .findById(examId)
                    .orElseThrow(() -> new RuntimeException("Exam not found"));

            exam.setExamStatus(ExamStatus.ENDED);
            examRepository.save(exam);
        }


}
