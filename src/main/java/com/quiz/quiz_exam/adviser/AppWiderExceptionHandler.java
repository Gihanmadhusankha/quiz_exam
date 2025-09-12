package com.quiz.quiz_exam.adviser;

import com.quiz.quiz_exam.exception.*;
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
    @ExceptionHandler(InvalidTimeException.class)
    public ResponseEntity<StandResponseDto>handleInvalidTimeException(InvalidTimeException e){
        return new ResponseEntity<>(
                new StandResponseDto(
                        400,e.getMessage(),e
                ),
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(ExamTimeOverException.class)
    public ResponseEntity<StandResponseDto>handleExamTimeOverException(ExamTimeOverException e){
        return new ResponseEntity<>(
                new StandResponseDto(
                        400,e.getMessage(),e
                ),
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(IncompleteExamException.class)
    public ResponseEntity<StandResponseDto>handleIncompleteExamException(IncompleteExamException e){
        return new ResponseEntity<>(
                new StandResponseDto(
                        400,e.getMessage(),e
                ),
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<StandResponseDto> handleUnauthorized(UnauthorizedAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new StandResponseDto(403, ex.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandResponseDto> handleOtherExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new StandResponseDto(500, "Something went wrong", null));
    }

    @ExceptionHandler(ExamAlreadyPublishedCompletedException.class)
    public ResponseEntity<StandResponseDto> handleExamAlreadyPublishedCompletedException(ExamAlreadyPublishedCompletedException e) {
        return new ResponseEntity<>(
                new StandResponseDto(
                        409, e.getMessage(), e
                ),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(ExamNotPublishedException.class)
    public ResponseEntity<StandResponseDto> handleExamNotPublishedException(ExamNotPublishedException e) {
        return new ResponseEntity<>(
                new StandResponseDto(
                        409, e.getMessage(), e
                ),
                HttpStatus.CONFLICT
        );
    }
}
