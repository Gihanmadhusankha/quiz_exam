package com.quiz.quiz_exam.service;

import com.quiz.quiz_exam.dto.requestDto.UserRequestDto;
import com.quiz.quiz_exam.dto.responseDto.UserResponseDto;
import com.quiz.quiz_exam.dto.responseDto.paginate.UserPaginateResponseDto;

public interface UserService {
    public UserResponseDto createUser(UserRequestDto userRequestDto);
    public UserResponseDto updateUser(int userId, UserRequestDto userRequestDto);

    public void deleteUser(int userId);

    public UserResponseDto findById(int userId);

    public UserPaginateResponseDto findAll(int page, int size, String searchText);


}
