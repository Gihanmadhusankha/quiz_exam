package com.quiz.quiz_exam.dto.responseDto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuestionResponseDto {
    private String questionText;
    private String option_a;
    private String option_b;
    private String option_c;
    private String option_d;
    private String correct_option;
}
