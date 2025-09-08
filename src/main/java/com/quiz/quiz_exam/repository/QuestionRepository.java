package com.quiz.quiz_exam.repository;

import com.quiz.quiz_exam.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question,Long>{

}