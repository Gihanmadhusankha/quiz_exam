package com.quiz.quiz_exam.entity;

import com.quiz.quiz_exam.enums.ExamStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="student_exam")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentExam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int  studentExamId;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;
    @OneToMany(mappedBy = "studentExam")
    private List<StudentAnswer> studentAnswers;

    @Column(name="status")
    private ExamStatus status;


}
