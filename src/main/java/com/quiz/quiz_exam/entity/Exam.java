package com.quiz.quiz_exam.entity;

import com.quiz.quiz_exam.enums.ExamStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="exam")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="exam_id",nullable = false)
    private String examId;
    @Column(name="title",nullable = false)
    private String title;
    @Column(name = "exam_date",nullable = false)
    private LocalDateTime examDate;
    @Column(name = "start_time",nullable = false)
    private LocalDateTime startTime;
    @Column(name = "end_time",nullable = false)
    private LocalDateTime endTime;
    @Column(name="status",nullable = false)
    private ExamStatus status;
    @ManyToOne
    @JoinColumn(name="teacher_id")
    private User teacher;
    @OneToMany(mappedBy = "exam")
    private List<Question> questions;


}
