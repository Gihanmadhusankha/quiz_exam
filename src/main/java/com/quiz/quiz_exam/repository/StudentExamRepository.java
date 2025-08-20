package com.quiz.quiz_exam.repository;

import com.quiz.quiz_exam.entity.StudentExam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentExamRepository extends JpaRepository<StudentExam,Long> {
    @Query("SELECT se FROM StudentExam se WHERE se.student.userId = :studentId AND " +
            "(LOWER(se.exam.title) LIKE LOWER(CONCAT('%', :search, '%')) OR :search IS NULL)")
    Page<StudentExam> findByStudentIdAndSearch(Long studentId, String search, Pageable pageable);


}
