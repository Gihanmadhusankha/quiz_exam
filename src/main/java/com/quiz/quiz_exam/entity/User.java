package com.quiz.quiz_exam.entity;

import com.quiz.quiz_exam.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column(name = "usen_name",nullable = false)
    private String name;
    @Column(name="user_email",nullable = false,unique = true)
    private String email;
    @Column(name = "user_password",nullable = false)
    private String password;
    @Column(name="user_role")
    private UserRole role;
    @OneToMany(mappedBy = "teacher")
    private List<Exam> exams;

    @OneToMany(mappedBy = "student")
    private List<StudentExam> studentExams;


}
