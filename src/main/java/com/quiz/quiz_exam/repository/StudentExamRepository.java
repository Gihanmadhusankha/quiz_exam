package com.quiz.quiz_exam.repository;

import com.quiz.quiz_exam.entity.Exam;
import com.quiz.quiz_exam.entity.StudentExam;
import com.quiz.quiz_exam.enums.ExamStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentExamRepository extends JpaRepository<StudentExam, Long> {



    // Find all by examId
    @Query("SELECT se FROM StudentExam se JOIN se.exam e  WHERE e.examId = :examId")
    List<StudentExam> findAllByExamId(@Param("examId") Long examId);

    @Query("SELECT se FROM StudentExam se JOIN se.student   s JOIN se.exam e  WHERE s.userId = :studentId AND e.examId=:examId")
    List<StudentExam> findByStudentIdAndExamId(
            @Param("studentId") Long studentId,
            @Param("examId") Long examId
    );

    // Count completed exams by examId
    @Query("SELECT COUNT(se) FROM StudentExam se JOIN se.exam e  WHERE e.examId = :examId AND se.studentExamStatus ='ATTENDED'")
    long countCompleted(@Param("examId") Long examId);

    // Find exams by teacherId
    @Query("SELECT se FROM StudentExam se JOIN se.exam e  WHERE  e.teacherId = :teacherId")
    List<StudentExam> findByTeacherId( @Param("teacherId") Long teacherId);


    @Query("SELECT se FROM StudentExam se  JOIN se.exam  e JOIN se.student s  WHERE e.examId = :examId AND s.userId= :studentId")
    Optional<StudentExam> findByExamIdAndStudentId(@Param("examId") Long examId, @Param("studentId") Long studentId);


    // Find exams by studentId with pagination
    @Query("SELECT se FROM StudentExam se JOIN se.student   s JOIN se.exam e  WHERE s.userId = :studentId ORDER BY e.date DESC")
   Page< StudentExam> findByStudent_Id(@Param("studentId") Long studentId, Pageable pageable);



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

    @Query("SELECT se FROM StudentExam se JOIN se.student   s JOIN se.exam e  WHERE s.userId = :studentId AND e.examId=:examId")
    Optional<StudentExam> findByStudentAndExam(Long studentId, Long examId);
}
