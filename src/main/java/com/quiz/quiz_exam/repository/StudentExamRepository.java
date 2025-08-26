package com.quiz.quiz_exam.repository;

import com.quiz.quiz_exam.entity.StudentExam;
import com.quiz.quiz_exam.enums.StudentExamStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentExamRepository extends JpaRepository<StudentExam, Long> {

    // Search exams by studentId and exam title
    @Query("SELECT se FROM StudentExam se WHERE se.student.userId = :studentId AND se.exam.title LIKE %:search%")
    Page<StudentExam> findByStudentIdAndSearch(@Param("studentId") Long studentId,
                                               @Param("search") String search,
                                               Pageable pageable);

    // Count all exams for a given examId
    @Query("SELECT COUNT(se) FROM StudentExam se WHERE se.exam.examId = :examId")
    long countAllByExamId(@Param("examId") Long examId);

    // Count by examId and status
    long countByExamExamIdAndStatus(Long examId, StudentExamStatus status);

    // Count by examId and studentId
    long countByExamExamIdAndStudent_UserId(Long examId, Long studentId);


    // Find all by examId
    @Query("SELECT se FROM StudentExam se WHERE se.exam.examId = :examId")
    List<StudentExam> findAllByExamId(@Param("examId") Long examId);

    // Count completed exams by examId
    @Query("SELECT COUNT(se) FROM StudentExam se WHERE se.exam.examId = :examId AND se.status = 'COMPLETED'")
    long countCompleted(@Param("examId") Long examId);

    // Find exams by teacherId
    List<StudentExam> findByExam_TeacherId(Long teacherId);

    // Find exams by studentId with pagination
    @Query("SELECT se FROM StudentExam se WHERE se.student.userId = :studentId")
    Page<StudentExam> findByStudent_Id(@Param("studentId") Long studentId, Pageable pageable);
}
