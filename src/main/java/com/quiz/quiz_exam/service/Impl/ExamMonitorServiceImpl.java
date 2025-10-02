package com.quiz.quiz_exam.service.Impl;

import com.quiz.quiz_exam.dto.ExamDtos;
import com.quiz.quiz_exam.dto.ExamMonitorDto;
import com.quiz.quiz_exam.dto.StudentDtos;
import com.quiz.quiz_exam.entity.Exam;
import com.quiz.quiz_exam.entity.StudentExam;
import com.quiz.quiz_exam.entity.User;
import com.quiz.quiz_exam.enums.ExamStatus;
import com.quiz.quiz_exam.enums.StudentExamStatus;
import com.quiz.quiz_exam.enums.UserRole;
import com.quiz.quiz_exam.exception.EntryNotfoundException;
import com.quiz.quiz_exam.exception.ExamTimeOverException;
import com.quiz.quiz_exam.repository.ExamRepository;
import com.quiz.quiz_exam.repository.StudentExamRepository;
import com.quiz.quiz_exam.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Override
    public ExamMonitorDto getExamMonitorData(Long teacherId,Long examId) {
        Exam exam =examRepository.findById(examId)
                .orElseThrow(()-> new EntryNotfoundException("Exam not found"));
        long completeCount=studentExamRepository.countCompleted(examId);
        List<StudentExam> studentExams = studentExamRepository.findAllByExamId(examId);
        List<User> students=userRepository.findByRole(UserRole.STUDENT);

        long totalCount = userRepository.countByRole(UserRole.STUDENT);

        String formatted;
        if (exam.getExamStatus()==ExamStatus.ENDED){
            formatted="Ended";
        }else {
            Duration remaining = Duration.between(LocalDateTime.now(), exam.getEndTime());
            if (remaining.isNegative()) {
                remaining = Duration.ZERO;
            }
            long minutes = remaining.toMinutes();
            long seconds = remaining.minusMinutes(minutes).getSeconds();
            formatted = String.format("%02d:%02d", minutes, seconds);
        }

        ExamMonitorDto dto = new ExamMonitorDto();
        dto.setExamName(exam.getTitle());
        dto.setExamStartTime(exam.getStartTime());
        dto.setExamEndTime(exam.getEndTime());
        dto.setCompletedCount(completeCount);
        dto.setTotalCount(totalCount);
        dto.setRemainingTime(formatted);
        dto.setStudents(students.stream()
                .map(s -> {
                    // Find StudentExam for this student (if exists)
                    StudentExam se = studentExams.stream()
                            .filter(e -> e.getStudent().getUserId().equals(s.getUserId()))
                            .findFirst()
                            .orElse(null);

                    String status;
                    if (se == null) {
                        status = "NOT_ATTENDED";
                    } else if (se.getStudentExamStatus() == StudentExamStatus.NEW || se.getStudentExamStatus() == StudentExamStatus.PENDING) {
                        status = "NOT_COMPLETED";
                    } else {
                        status ="COMPLETED";
                    }
                    return new StudentDtos.StudentInfo(
                            s.getUserId(),
                            se != null ? se.getStudentExamId() : null,
                            s.getName(),
                            status
                    );
                })
                .toList()
        );



        return dto;
    }

    @Override
    public void endExam(Long teacherId, ExamDtos.Request req) {

            Exam exam=examRepository.findByExamId(req.examId())
                    .orElseThrow(() -> new EntryNotfoundException("Exam not found"));
            if(req.isTimerEnd()){
                exam.setExamStatus(ExamStatus.ENDED);
                examRepository.save(exam);
                throw new ExamTimeOverException("Exam time is over, exam ended automatically");
            }
            //manually teacher end the exam
            exam.setExamStatus(ExamStatus.ENDED);
            examRepository.save(exam);
        }


}
