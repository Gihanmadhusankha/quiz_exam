package com.quiz.quiz_exam.repository;

import com.quiz.quiz_exam.entity.Exam;
import com.quiz.quiz_exam.entity.User;
import com.quiz.quiz_exam.enums.ExamStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query("SELECT e FROM Exam e WHERE e.status = :status AND e.title LIKE %:search%")
    Page<Exam> findPublishedExams(@Param("status") ExamStatus status,
                                  @Param("search") String search,
                                  Pageable pageable);

    @Query("SELECT e FROM Exam e WHERE e.teacherId = :teacherId")
    Page<Exam> findByTeacherId(@Param("teacherId") Long teacherId,
                               Pageable pageable);



    @Query("SELECT e FROM Exam e WHERE e.teacherId = :teacherId AND e.title LIKE %:search%")
    Page<Exam> findByTeacherIdAndSearch(@Param("teacherId") Long teacherId,
                                        @Param("search") String search,
                                        Pageable pageable);


    @Query("SELECT e FROM Exam e WHERE e.teacherId = :teacherId")
    List<Exam> findByTeachId(@Param("teacherId") Long teacherId);


    List<Exam> findByExamStatus(ExamStatus examStatus);
}
