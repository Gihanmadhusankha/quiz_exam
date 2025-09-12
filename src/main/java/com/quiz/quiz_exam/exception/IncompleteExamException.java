package com.quiz.quiz_exam.exception;

//For incomplete  exam submission
public class IncompleteExamException extends RuntimeException {
    public IncompleteExamException(String message) {
        super(message);
    }
}
