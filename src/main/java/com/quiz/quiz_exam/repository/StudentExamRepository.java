package com.quiz.quiz_exam.repository;

import com.quiz.quiz_exam.entity.StudentExam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentExamRepository extends JpaRepository<StudentExam,Integer> {
}
