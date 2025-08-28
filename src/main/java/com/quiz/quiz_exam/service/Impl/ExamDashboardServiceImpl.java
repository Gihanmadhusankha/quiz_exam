package com.quiz.quiz_exam.service.Impl;

import com.quiz.quiz_exam.dto.DashboardDto;
import com.quiz.quiz_exam.entity.StudentAnswer;
import com.quiz.quiz_exam.entity.StudentExam;
import com.quiz.quiz_exam.entity.User;
import com.quiz.quiz_exam.exception.EntryNotfoundException;
import com.quiz.quiz_exam.repository.ExamRepository;
import com.quiz.quiz_exam.repository.StudentExamRepository;


import com.quiz.quiz_exam.repository.UserRepository;
import com.quiz.quiz_exam.service.ExamDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamDashboardServiceImpl implements ExamDashboardService {


    private final StudentExamRepository studentExamRepository;
    private final UserRepository userRepository;

    @Override
    public DashboardDto.DashboardResponse getDashboard(Long teacherId) {
        List<DashboardDto.ProgressOverTime> progress = getExamResultsOverTime(teacherId);
        List<DashboardDto.GradeDistribution> grades = getGradeDistribution(teacherId);
        List<DashboardDto.studentAverage> top = getTopStudents(teacherId);
        List<DashboardDto.studentAverage> low = getLowStudents(teacherId);
        return new DashboardDto.DashboardResponse(progress,grades,top,low);
    }


    private List<DashboardDto.ProgressOverTime> getExamResultsOverTime(Long teacherId) {
        List<StudentExam> allStudentExams = studentExamRepository.findByExam_TeacherId(teacherId);

        // Group by exam
        Map<Long, List<StudentExam>> examsMap = allStudentExams.stream()
                .collect(Collectors.groupingBy(se -> se.getExam().getExamId()));

        return examsMap.entrySet().stream()
                .map(entry -> {
                    Long examId = entry.getKey();
                    List<StudentExam> studentExams = entry.getValue();

                    double avgScore = studentExams.stream()
                            .mapToDouble(this::calculateScore)
                            .average()
                            .orElse(0.0);

                    LocalDateTime examDate = studentExams.get(0).getExam().getDate();
                    String examTitle = studentExams.get(0).getExam().getTitle();

                    return new DashboardDto.ProgressOverTime(
                            examTitle,
                            examDate,
                            avgScore,
                            (long) studentExams.size()
                    );
                })
                .sorted(Comparator.comparing(DashboardDto.ProgressOverTime::examDate))
                .collect(Collectors.toList());
    }


    private List<DashboardDto.GradeDistribution> getGradeDistribution(Long teacherId) {
        List<StudentExam> allStudentExams = studentExamRepository.findByExam_TeacherId(teacherId);

        Map<String, Long> gradeCount = new HashMap<>();
        gradeCount.put("A", 0L);
        gradeCount.put("B", 0L);
        gradeCount.put("C", 0L);
        gradeCount.put("D", 0L);
        gradeCount.put("F", 0L);

        allStudentExams.forEach(se -> {
            double score = calculateScore(se);
            String grade;
            if (score >= 90) grade = "A";
            else if (score >= 80) grade = "B";
            else if (score >= 70) grade = "C";
            else if (score >= 60) grade = "D";
            else grade = "F";

            gradeCount.put(grade, gradeCount.get(grade) + 1);
        });

        long totalStudents = allStudentExams.size();

        return gradeCount.entrySet().stream()
                .map(e -> new DashboardDto.GradeDistribution(
                        e.getKey(),
                        e.getValue(),
                        totalStudents > 0 ? (e.getValue() * 100.0 / totalStudents) : 0.0
                ))
                .collect(Collectors.toList());
    }


    private List<DashboardDto.studentAverage> getTopStudents(Long teacherId) {
        return getStudentAverages(teacherId).stream()
                .sorted(Comparator.comparingDouble(DashboardDto.studentAverage::averageScore).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }


    private List<DashboardDto.studentAverage> getLowStudents(Long teacherId ) {
        return getStudentAverages(teacherId).stream()
                .sorted(Comparator.comparingDouble(DashboardDto.studentAverage::averageScore))
                .limit(5)
                .collect(Collectors.toList());
    }


    private double calculateScore(StudentExam studentExam) {
        List<StudentAnswer> answers = studentExam.getStudentAnswers();
        if (answers == null || answers.isEmpty()) return 0.0;

        long correctCount = answers.stream().filter(StudentAnswer::is_correct).count();
        return ((double) correctCount / answers.size()) * 100;
    }

    private List<DashboardDto.studentAverage> getStudentAverages(Long teacherId) {
        // Fetch all StudentExams for this teacher
        List<StudentExam> allStudentExams = studentExamRepository.findByExam_TeacherId(teacherId);

        // Group scores by student
        Map<Long, List<Double>> studentScores = new HashMap<>();
        for (StudentExam se : allStudentExams) {
            Long studentId = se.getStudent().getUserId();
            studentScores.computeIfAbsent(studentId, k -> new ArrayList<>())
                    .add(calculateScore(se));
        }

        // Convert to DTOs with average
        List<DashboardDto.studentAverage> averages = new ArrayList<>();
        for (Map.Entry<Long, List<Double>> entry : studentScores.entrySet()) {
            User student = userRepository.findById(entry.getKey()).orElse(null);
            if (student != null) {
                double avg = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                averages.add(new DashboardDto.studentAverage(
                        entry.getKey(),
                        student.getName(),
                        avg
                ));
            }
        }

        return averages;
    }


}
