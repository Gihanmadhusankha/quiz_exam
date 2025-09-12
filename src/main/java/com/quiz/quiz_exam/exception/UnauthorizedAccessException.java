package com.quiz.quiz_exam.exception;

//For unauthorized access
public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
