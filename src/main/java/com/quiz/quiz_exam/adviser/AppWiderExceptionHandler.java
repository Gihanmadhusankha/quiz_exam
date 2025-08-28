package com.quiz.quiz_exam.adviser;

import com.quiz.quiz_exam.exception.EntryNotfoundException;
import com.quiz.quiz_exam.util.StandResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppWiderExceptionHandler {
    @ExceptionHandler(EntryNotfoundException.class)
    public ResponseEntity<StandResponseDto>handleEntryNotfoundException(EntryNotfoundException e){
        return new ResponseEntity<>(
                new StandResponseDto(
                        404,e.getMessage(),e
                ),
                HttpStatus.NOT_FOUND
        );
    }
}
