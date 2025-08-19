package com.quiz.quiz_exam.dto;


import com.quiz.quiz_exam.enums.UserRole;
import jakarta.validation.constraints.*;

public class AuthDtos {
    public record RegisterRequest(
            @NotBlank String name,
            @Email @NotBlank String email,
            @Size(min = 6) String password,
            UserRole role
    ) { }

    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) { }

    public record AuthResponse(String token, Long userId, String name, String email, UserRole role) { }
}
