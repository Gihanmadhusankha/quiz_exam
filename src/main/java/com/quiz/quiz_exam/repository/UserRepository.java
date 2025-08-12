package com.quiz.quiz_exam.repository;

import com.quiz.quiz_exam.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("SELECT u FROM User u " +
            "WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "   OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchText, '%'))")
    Page<User> searchAllUsers(@Param("searchText") String searchText, Pageable pageable);
}
