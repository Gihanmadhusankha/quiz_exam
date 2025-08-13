package com.quiz.quiz_exam.dto.requestDto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class QuestionRequestDto {
    private String questionText;
    private String option_a;
    private String option_b;
    private String option_c;
    private String option_d;
    private String correct_option;
}
