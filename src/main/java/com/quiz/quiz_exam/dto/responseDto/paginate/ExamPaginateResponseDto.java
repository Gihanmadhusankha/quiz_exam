package com.quiz.quiz_exam.dto.responseDto.paginate;

import com.quiz.quiz_exam.dto.responseDto.ExamResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExamPaginateResponseDto {
    private List<ExamResponseDto> dataList;
}
