package com.quiz.quiz_exam.service.Impl;

import com.quiz.quiz_exam.dto.AuthDtos;

public interface AuthService {
    AuthDtos.AuthResponse register(AuthDtos.RegisterRequest req);
    AuthDtos.AuthResponse login(AuthDtos.LoginRequest req);
}
