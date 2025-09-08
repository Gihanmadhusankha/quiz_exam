package com.quiz.quiz_exam.controller;

import com.quiz.quiz_exam.dto.AuthDtos;
import com.quiz.quiz_exam.service.Impl.AuthService;
import com.quiz.quiz_exam.util.StandResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {
    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<StandResponseDto> register(@Valid @RequestBody AuthDtos.RegisterRequest req) {

        return  new ResponseEntity<>(
                new StandResponseDto(
                        201, "user register successfully", authService.register(req)
                ),
                HttpStatus.CREATED
                );
    }

    @PostMapping("/login")
    public ResponseEntity<StandResponseDto> login(@Valid @RequestBody AuthDtos.LoginRequest req) {
        return  new ResponseEntity<>(
                new StandResponseDto(200,"user Login Successfully",authService.login(req)
                ),
                HttpStatus.OK
        );
    }
}
