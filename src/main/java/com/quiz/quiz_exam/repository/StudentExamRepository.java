package com.quiz.quiz_exam.repository;

import com.quiz.quiz_exam.entity.Exam;
import com.quiz.quiz_exam.entity.StudentExam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentExamRepository extends JpaRepository<StudentExam,Long> {
    @Query("SELECT se FROM StudentExam se WHERE se.student.userId = :studentId AND " +
            "(LOWER(se.exam.title) LIKE LOWER(CONCAT('%', :search, '%')) OR :search IS NULL)")
    Page<StudentExam> findByStudentIdAndSearch(Long studentId, String search, Pageable pageable);

    @Query("SELECT COUNT(se) FROM StudentExam se WHERE se.exam.examId = :examId")
    long countAllByExamId(@Param("examId") Long examId);

    @Query("SELECT COUNT(se) FROM StudentExam se WHERE se.exam.examId = :examId AND se.status = com.quiz.quiz_exam.enums.StudentExamStatus.PENDING")
    long countPendingByExamId(@Param("examId") Long examId);

    @Query("SELECT COUNT(se) FROM StudentExam se WHERE se.exam.examId = :examId AND se.status = com.quiz.quiz_exam.enums.StudentExamStatus.ATTENDED")
    long countAttendedByExamId(@Param("examId") Long examId);

    @Query("SELECT se FROM StudentExam se WHERE se.exam.examId = :examId")
    List<StudentExam> findAllByExamId(@Param("examId") Long examId);



    List<StudentExam> findByExam_ExamId(Long examId);
}
