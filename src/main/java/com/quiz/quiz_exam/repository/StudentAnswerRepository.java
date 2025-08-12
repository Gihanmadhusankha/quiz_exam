package com.quiz.quiz_exam.repository;

import com.quiz.quiz_exam.entity.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer,Integer> {
}
