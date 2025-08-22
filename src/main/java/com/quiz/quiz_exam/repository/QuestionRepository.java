package com.quiz.quiz_exam.repository;

import com.quiz.quiz_exam.entity.Exam;
import com.quiz.quiz_exam.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question,Long> {
    List<Question> findByExam_ExamId(Long examId);

    Optional<Question> findFirstByExamAndQuestionIdNotIn(Exam exam, List<Long> questionIds);

}
