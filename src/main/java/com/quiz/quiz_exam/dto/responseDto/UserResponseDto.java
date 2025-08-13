package com.quiz.quiz_exam.dto.responseDto;

import com.quiz.quiz_exam.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserResponseDto
{

    private int userId;
    private String name;
    private String email;
    private String password;
    private UserRole role;
}
