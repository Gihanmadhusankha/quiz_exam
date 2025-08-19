package com.quiz.quiz_exam.repository;

import com.quiz.quiz_exam.dto.ExamDtos;
import com.quiz.quiz_exam.entity.Exam;
import com.quiz.quiz_exam.enums.ExamStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam,Long> {


    List<Exam> findByTeacherId(Long teacherId);
}
