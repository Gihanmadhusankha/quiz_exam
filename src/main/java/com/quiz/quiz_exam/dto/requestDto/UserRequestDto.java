package com.quiz.quiz_exam.dto.requestDto;

import com.quiz.quiz_exam.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRequestDto {

    private String name;
    private String email;
    private String password;
    private UserRole role;

}