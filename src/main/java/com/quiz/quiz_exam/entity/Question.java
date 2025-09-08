package com.quiz.quiz_exam.entity;

import com.quiz.quiz_exam.enums.RecordStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="question")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @Column(name="question_text", nullable = false)
    private String questionText;

    @Column(name="options_a", nullable = false)
    private String optionA;

    @Column(name="options_b", nullable = false)
    private String optionB;

    @Column(name="options_c", nullable = false)
    private String optionC;

    @Column(name="options_d", nullable = false)
    private String optionD;

    @Column(name="correct_answer", nullable = false)
    private String correctOption;

    @Column(name="status",nullable = false)
    @Enumerated(EnumType.STRING)
    private RecordStatus status=RecordStatus.ONLINE;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;
}
