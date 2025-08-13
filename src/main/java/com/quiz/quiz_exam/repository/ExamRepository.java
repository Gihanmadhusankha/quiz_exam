package com.quiz.quiz_exam.repository;

import com.quiz.quiz_exam.entity.Exam;
import com.quiz.quiz_exam.enums.ExamStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam,Long> {







}
