package com.quiz.quiz_exam.dto.responseDto.paginate;

import com.quiz.quiz_exam.dto.responseDto.UserResponseDto;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class UserPaginateResponseDto {
    private List<UserResponseDto> dataList;
}
