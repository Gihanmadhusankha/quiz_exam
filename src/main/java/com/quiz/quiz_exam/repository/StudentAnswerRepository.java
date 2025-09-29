package com.quiz.quiz_exam.repository;

import com.quiz.quiz_exam.entity.Question;
import com.quiz.quiz_exam.entity.StudentAnswer;
import com.quiz.quiz_exam.entity.StudentExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer,Long> {


    @Query("SELECT sa FROM StudentAnswer sa  JOIN sa.studentExam se JOIN sa.question q WHERE se.studentExamId = :studentExamId AND q.questionId = :questionId")
    Optional<StudentAnswer> findByStudentExamAndQuestion(@Param("studentExamId") Long studentExamId,
                                                         @Param("questionId") Long questionId);




    @Query("SELECT COUNT(sa) FROM StudentAnswer sa JOIN sa.studentExam se  WHERE se= :studentExam AND sa.is_correct = true")
    long countCorrectAnswers(@Param("studentExam") StudentExam studentExam);

    @Query("SELECT sa FROM StudentAnswer sa JOIN  sa.studentExam se WHERE se.studentExamId = :studentExamId")
    List<StudentAnswer> findByStudentExamId(@Param("studentExamId") Long studentExamId);


    @Query("SELECT sa.question.questionId FROM StudentAnswer sa WHERE sa.studentExam.studentExamId=:studentExamId  ORDER BY sa.studentAnswerId DESC LIMIT 1")
    Optional<Long>findLastAnsweredQuestionId(@Param("studentExamId")Long studentExamId);

}
