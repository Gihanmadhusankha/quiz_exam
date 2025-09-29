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

public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query("SELECT e FROM Exam e WHERE e.status = :status AND e.title LIKE %:search%")
    Page<Exam> findPublishedExams(@Param("status") ExamStatus status,
                                  @Param("search") String search,
                                  Pageable pageable);

    @Query("SELECT e FROM Exam e WHERE e.teacherId = :teacherId ORDER BY e.date DESC")
    Page<Exam> findByTeacherId(@Param("teacherId") Long teacherId,
                               Pageable pageable);



    @Query("SELECT e FROM Exam e WHERE e.teacherId = :teacherId AND e.title LIKE %:search% ORDER BY e.date DESC")
    Page<Exam> findByTeacherIdAndSearch(@Param("teacherId") Long teacherId,
                                        @Param("search") String search,
                                        Pageable pageable);

    @Query("SELECT e FROM Exam e WHERE e.teacherId = :teacherId")
    List<Exam> findByTeachId(@Param("teacherId") Long teacherId);


    @Query("SELECT e FROM Exam e WHERE e.examId = :examId")
    Optional<Exam> findByExamId(@Param("examId")long examId);



    List<Exam> findByExamStatusIn(List<ExamStatus> statuses);
    @Query("SELECT e FROM Exam e WHERE  e.examStatus <> 'DRAFT'  AND  e.title LIKE %:search% ORDER BY e.date DESC")
    Page<Exam >findNotDraftExamsAndSearch(
            @Param("search") String search,
            Pageable pageable);

    @Query("SELECT e FROM Exam e WHERE  e.examStatus <> 'DRAFT'   ORDER BY e.date DESC")
    Page<Exam >findNotDraftExams(
            Pageable pageable);
}
