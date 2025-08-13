package com.quiz.quiz_exam.advisor;

import com.quiz.quiz_exam.exceptions.EntryNotFoundException;
import com.quiz.quiz_exam.util.StandardResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class AppWiderExceptionHandler {
    @ExceptionHandler(EntryNotFoundException.class)
    public ResponseEntity<StandardResponseDto> handleEntryNotFoundException(EntryNotFoundException e){
        return  new ResponseEntity<>(
                new StandardResponseDto(
                        404,e.getMessage(),e
                ),
                HttpStatus.NOT_FOUND
        );
    }
}
