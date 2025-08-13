package com.quiz.quiz_exam.repository;

import com.quiz.quiz_exam.entity.Exam;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam,Integer> {
    @Query(value ="SELECT * FROM exam WHERE  exam_id LIKE %?1% " , nativeQuery =true )
    Optional<Exam> findExam(String examId);

    @Query(value ="SELECT * FROM exam WHERE  exam_name OR status LIKE %?1% " , nativeQuery =true )

    Page<Exam> searchAllExams(String searchText, Pageable pageable);
}
