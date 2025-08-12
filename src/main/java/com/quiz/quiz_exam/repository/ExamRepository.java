package com.quiz.quiz_exam.repository;

import com.quiz.quiz_exam.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam,Integer> {
}
