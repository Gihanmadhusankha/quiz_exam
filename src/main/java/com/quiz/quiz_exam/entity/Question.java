package com.quiz.quiz_exam.entity;

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
    private int questionId;
    @Column(name="question_Text",nullable = false)
    private String questionText;
    @Column(name="options_A",nullable = false)
    private String option_a;
    @Column(name="options_B",nullable = false)
    private String option_b;
    @Column(name = "options_C",nullable = false)
    private String option_c;
    @Column(name = "option_D",nullable = false)
    private String option_d;
    @Column(name="correct_Answer",nullable = false)
    private String correct_option;
    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;
}
