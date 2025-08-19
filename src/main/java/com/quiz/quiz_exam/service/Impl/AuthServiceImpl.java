package com.quiz.quiz_exam.service.Impl;

import com.quiz.quiz_exam.dto.AuthDtos;
import com.quiz.quiz_exam.entity.User;
import com.quiz.quiz_exam.enums.UserRole;
import com.quiz.quiz_exam.repository.UserRepository;
import com.quiz.quiz_exam.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements com.quiz.quiz_exam.service.Impl.AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil;


//user register
    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest req) {
        if (userRepository.findByEmail(req.email()).isPresent()) throw new RuntimeException("Email already exists");
        User u = User.builder()
                .name(req.name()).email(req.email())
                .password(encoder.encode(req.password()))
                .role(req.role() == null ? UserRole.STUDENT : req.role())
                .build();
        u = userRepository.save(u);
        String token = jwtUtil.generateToken(u.getEmail(), Map.of("uid", u.getUserId(), "role", u.getRole().name()));
        return new AuthDtos.AuthResponse(token, u.getUserId(), u.getName(), u.getEmail(), u.getRole());
    }
//user login
    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest req) {
        var u = userRepository.findByEmail(req.email()).orElseThrow(() -> new RuntimeException("Invalid emails "));
        if (!encoder.matches(req.password(), u.getPassword())) throw new RuntimeException("Invalid password");
        String token = jwtUtil.generateToken(u.getEmail(), Map.of("uid", u.getUserId(), "role", u.getRole().name()));
        return new AuthDtos.AuthResponse(token, u.getUserId(), u.getName(), u.getEmail(), u.getRole());
    }
}
