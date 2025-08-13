package com.quiz.quiz_exam.service.Impl;

import com.quiz.quiz_exam.dto.requestDto.UserRequestDto;
import com.quiz.quiz_exam.dto.responseDto.UserResponseDto;
import com.quiz.quiz_exam.dto.responseDto.paginate.UserPaginateResponseDto;
import com.quiz.quiz_exam.entity.User;
import com.quiz.quiz_exam.exceptions.EntryNotFoundException;
import com.quiz.quiz_exam.repository.UserRepository;
import com.quiz.quiz_exam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User savedUser = userRepository.save(toUser(userRequestDto));
        return toUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDto updateUser(int userId, UserRequestDto userRequestDto) {
        User selectedUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntryNotFoundException("User Not found"));

        selectedUser.setName(userRequestDto.getName());
        selectedUser.setEmail(userRequestDto.getEmail());
        selectedUser.setPassword(userRequestDto.getPassword());
        selectedUser.setRole(userRequestDto.getRole());

        userRepository.save(selectedUser);
        return toUserResponseDto(selectedUser);
    }

    @Override
    public void deleteUser(int userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntryNotFoundException("User Not found"));
        userRepository.deleteById(userId);
    }

    @Override
    public UserResponseDto findById(int userId) {
        User selectedUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntryNotFoundException("User Not found"));
        return toUserResponseDto(selectedUser);
    }

    @Override
    public UserPaginateResponseDto findAll(int page, int size, String searchText) {
        var resultPage = userRepository.searchAllUsers(searchText, PageRequest.of(page, size));
        return UserPaginateResponseDto.builder()
                .dataList(resultPage.getContent().stream()
                        .map(this::toUserResponseDto)
                        .collect(Collectors.toList()))

                .build();
    }

    private User toUser(UserRequestDto dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(dto.getRole())
                .build();
    }

    private UserResponseDto toUserResponseDto(User user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
