package com.quiz.quiz_exam.entity;

import com.quiz.quiz_exam.enums.RecordStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="student_answer")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StudentAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentAnswerId;

    private boolean is_correct;
    private String selected_option;
    @ManyToOne
    @JoinColumn(name = "student_exam_id")
    private StudentExam studentExam;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name="status",nullable = false)
    @Enumerated(EnumType.STRING)
    private RecordStatus status=RecordStatus.ONLINE;


}
