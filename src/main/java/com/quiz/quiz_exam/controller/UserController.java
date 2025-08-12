package com.quiz.quiz_exam.controller;

import com.quiz.quiz_exam.dto.requestDto.UserRequestDto;
import com.quiz.quiz_exam.dto.responseDto.UserResponseDto;
import com.quiz.quiz_exam.service.UserService;
import com.quiz.quiz_exam.util.StandardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping
    public ResponseEntity<StandardResponseDto> addUser(@RequestBody UserRequestDto userRequestDto) {
        UserResponseDto createdUser = userService.createUser(userRequestDto);
        return new ResponseEntity<>(
                new StandardResponseDto(201, "User created", createdUser),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponseDto> findById(@PathVariable("id") int userId) {
        return ResponseEntity.ok(
                new StandardResponseDto(200, "User data", userService.findById(userId))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<StandardResponseDto> updateById(
            @PathVariable("id") int userId,
            @RequestBody UserRequestDto userRequestDto
    ) {
        userService.updateUser(userId, userRequestDto);
        return ResponseEntity.ok(
                new StandardResponseDto(200, "User updated", null)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<StandardResponseDto> searchUsers(
            @RequestParam String searchText,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ResponseEntity.ok(
                new StandardResponseDto(200, "User data", userService.findAll(page, size, searchText))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") int userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
