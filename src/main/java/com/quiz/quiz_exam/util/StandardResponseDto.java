package com.quiz.quiz_exam.util;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StandardResponseDto {
    private int code;
    private String message;
    private Object data;
}
