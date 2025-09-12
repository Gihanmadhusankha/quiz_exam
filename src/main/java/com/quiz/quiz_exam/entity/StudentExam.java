package com.quiz.quiz_exam.entity;

import com.quiz.quiz_exam.enums.RecordStatus;
import com.quiz.quiz_exam.enums.StudentExamStatus;
import jakarta.persistence.*;
import lombok.*;

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

    @Enumerated(EnumType.STRING)
    @Column(name="studentExam_status",nullable = false,length=20)
    private StudentExamStatus studentExamStatus=StudentExamStatus.NEW;

    @Column(name="status",nullable = false)
    @Enumerated(EnumType.STRING)
    private RecordStatus status=RecordStatus.ONLINE;
    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;


}
