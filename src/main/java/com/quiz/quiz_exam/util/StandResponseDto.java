package com.quiz.quiz_exam.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StandResponseDto {
    private int code;
    private String message;
    private Object data;


}
