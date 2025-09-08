package com.quiz.quiz_exam.service.Impl;

import com.quiz.quiz_exam.dto.DashboardDto;
import com.quiz.quiz_exam.entity.StudentAnswer;
import com.quiz.quiz_exam.entity.StudentExam;
import com.quiz.quiz_exam.entity.User;
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

        // Fetch StudentExams along with answers using fetch join
        List<StudentExam> studentExams = studentExamRepository
                .findByTeacherId(teacherId);

        List<DashboardDto.ProgressOverTime> progress = getProgress(studentExams);
        List<DashboardDto.GradeDistribution> grades = getGradeDistribution(studentExams);
        List<DashboardDto.studentAverage> top = getTopStudents(studentExams);
        List<DashboardDto.studentAverage> low = getLowStudents(studentExams);

        return new DashboardDto.DashboardResponse(progress, grades, top, low);
    }

    private List<DashboardDto.ProgressOverTime> getProgress(List<StudentExam> studentExams) {
        if (studentExams.isEmpty()) return Collections.emptyList();

        double avgScore = studentExams.stream()
                .mapToDouble(this::calculateScore)
                .average()
                .orElse(0.0);

        LocalDateTime examDate = studentExams.get(0).getExam().getDate();
        String examTitle = studentExams.get(0).getExam().getTitle();

        return List.of(new DashboardDto.ProgressOverTime(
                examTitle,
                examDate,
                avgScore,
                (long) studentExams.size()
        ));
    }

    private List<DashboardDto.GradeDistribution> getGradeDistribution(List<StudentExam> studentExams) {
        Map<String, Long> gradeCount = new HashMap<>(Map.of(
                "A", 0L, "B", 0L, "C", 0L, "D", 0L, "F", 0L));

        studentExams.forEach(se -> {
            double score = calculateScore(se);
            String grade;
            if (score >= 85) grade = "A";
            else if (score >= 65) grade = "B";
            else if (score >= 55) grade = "C";
            else if (score >= 35) grade = "D";
            else grade = "F";

            gradeCount.put(grade, gradeCount.get(grade) + 1);
        });

        long total = studentExams.size();

        return gradeCount.entrySet().stream()
                .map(e -> new DashboardDto.GradeDistribution(
                        e.getKey(),
                        e.getValue(),
                        total > 0 ? (e.getValue() * 100.0 / total) : 0.0
                ))
                .collect(Collectors.toList());
    }

    private List<DashboardDto.studentAverage> getTopStudents(List<StudentExam> studentExams) {
        return getStudentAverages(studentExams).stream()
                .sorted(Comparator.comparingDouble(DashboardDto.studentAverage::averageScore).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    private List<DashboardDto.studentAverage> getLowStudents(List<StudentExam> studentExams) {
        return getStudentAverages(studentExams).stream()
                .sorted(Comparator.comparingDouble(DashboardDto.studentAverage::averageScore))
                .limit(5)
                .collect(Collectors.toList());
    }

    private double calculateScore(StudentExam studentExam) {
        if (studentExam.getStudentAnswers() == null || studentExam.getStudentAnswers().isEmpty())
            return 0.0;

        long correctCount = studentExam.getStudentAnswers().stream()
                .filter(a -> a.getQuestion().getCorrectOption().equalsIgnoreCase(a.getSelected_option()))
                .count();

        return ((double) correctCount / studentExam.getStudentAnswers().size()) * 100;
    }


    private List<DashboardDto.studentAverage> getStudentAverages(List<StudentExam> studentExams) {
        Map<Long, List<Double>> studentScores = new HashMap<>();

        for (StudentExam se : studentExams) {
            Long studentId = se.getStudent().getUserId();
            studentScores.computeIfAbsent(studentId, k -> new ArrayList<>())
                    .add(calculateScore(se));
        }

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
