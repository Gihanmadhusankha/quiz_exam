package com.quiz.quiz_exam.entity;

import com.quiz.quiz_exam.enums.ExamStatus;
import com.quiz.quiz_exam.enums.StudentExamStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="student_exam")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StudentExam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  studentExamId;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;
    @OneToMany(mappedBy = "studentExam")
    private List<StudentAnswer> studentAnswers;

    @Column(name="status")
    private StudentExamStatus status=StudentExamStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

}
