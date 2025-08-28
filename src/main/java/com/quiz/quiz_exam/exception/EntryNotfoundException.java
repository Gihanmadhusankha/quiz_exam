package com.quiz.quiz_exam.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class EntryNotfoundException extends RuntimeException {
     public EntryNotfoundException(String message){
         super(message);
     }
}
