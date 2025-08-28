package com.quiz.quiz_exam.entity;

import com.quiz.quiz_exam.enums.ExamStatus;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private Long examId;

    private Long teacherId;

    @Column(name="last_updated")
    private LocalDateTime lastUpdated;


    @Column(name="title", nullable = false)
    private String title;

    @Column(name = "exam_date", nullable = false)
    private LocalDateTime date;

    @Column(name = "exam_start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "exam_end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name="exam_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ExamStatus status = ExamStatus.DRAFT;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @PrePersist
    @PreUpdate
    public void setTimestamps() {
        this.lastUpdated = LocalDateTime.now();
    }

}
