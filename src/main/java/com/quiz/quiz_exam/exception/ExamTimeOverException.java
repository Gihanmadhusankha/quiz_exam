package com.quiz.quiz_exam.exception;

//for when student submits after exam ends
public class ExamTimeOverException extends RuntimeException {
    public ExamTimeOverException(String message) {
        super(message);
    }
}
