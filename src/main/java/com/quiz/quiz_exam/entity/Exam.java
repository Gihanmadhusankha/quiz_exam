package com.quiz.quiz_exam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="exam")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int examId;
    @Column(name="exam_id",nullable = false)
    private String title;
    @Column(name = "exam_date",nullable = false)
    private LocalDateTime date;
    @Column(name = "start_time",nullable = false)
    private LocalDateTime startTime;
    @Column(name = "end_time",nullable = false)
    private LocalDateTime endTime;
    @ManyToOne
    @JoinColumn(name="teacher_id")
    private User teacherId;
    @OneToMany(mappedBy = "exam")
    private List<Question> questions;


}
