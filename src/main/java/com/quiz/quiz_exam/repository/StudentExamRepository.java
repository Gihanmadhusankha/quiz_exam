package com.quiz.quiz_exam.repository;

import com.quiz.quiz_exam.entity.StudentExam;
import com.quiz.quiz_exam.enums.StudentExamStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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
    @Query("SELECT COUNT(se) FROM StudentExam se WHERE se.exam.examId = :examId AND se.studentExamStatus = 'COMPLETED'")
    long countCompleted(@Param("examId") Long examId);

    // Find exams by teacherId
    @Query("SELECT se FROM StudentExam se WHERE  se.exam.teacherId = :teacherId")
    List<StudentExam> findByTeacherId( @Param("teacherId") Long teacherId);


    @Query("SELECT se FROM StudentExam se WHERE se.exam.examId = :examId AND se.student.userId= :studentId")
    Optional<StudentExam> findByExamIdAndStudentId(@Param("examId") Long examId, @Param("studentId") Long studentId);


    // Find exams by studentId with pagination
    @Query("SELECT se FROM StudentExam se WHERE se.student.userId = :studentId")
   Page< StudentExam> findByStudent_Id(@Param("studentId") Long studentId, Pageable pageable);

   @Query("SELECT  se FROM StudentExam se JOIN se.exam e WHERE  se.student.userId=:studentId AND e.examStatus='PUBLISHED'")
   Page<StudentExam>findPublishedExamsByStudent( @Param("studentId") Long studentId,Pageable pageable);

    @Query("SELECT COUNT(se) > 0 FROM StudentExam se WHERE se.student.userId = :studentId AND se.exam.examId = :examId")
    boolean existsByStudentAndExam(@Param("studentId") Long studentId, @Param("examId") Long examId);

    @Query("SELECT DISTINCT se FROM StudentExam se " +
            "LEFT JOIN FETCH se.studentAnswers sa " +
            "JOIN se.exam e " +
            "WHERE e.examId = :examId AND e.teacherId = :teacherId")
    List<StudentExam> findByExamIdAndTeacherIdWithAnswers(
            @Param("examId") Long examId,
            @Param("teacherId") Long teacherId
    );

    Optional<Object> findByStudent_UserIdAndExam_ExamId(Long studentId, Long examId);




}
