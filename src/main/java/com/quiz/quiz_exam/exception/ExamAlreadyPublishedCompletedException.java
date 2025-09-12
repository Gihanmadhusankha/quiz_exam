package com.quiz.quiz_exam.exception;

public class ExamAlreadyPublishedCompletedException extends RuntimeException {
    public ExamAlreadyPublishedCompletedException(String message) {
        super(message);
    }
}
