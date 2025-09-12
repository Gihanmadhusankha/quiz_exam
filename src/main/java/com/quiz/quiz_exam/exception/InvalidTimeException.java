package com.quiz.quiz_exam.exception;

//For exam date/time validation
public class InvalidTimeException extends RuntimeException {
    public InvalidTimeException(String message) {
        super(message);
    }
}

