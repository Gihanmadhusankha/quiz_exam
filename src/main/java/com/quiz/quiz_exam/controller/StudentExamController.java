package com.quiz.quiz_exam.controller;

import com.quiz.quiz_exam.dto.ResultDtos;
import com.quiz.quiz_exam.dto.StudentDtos;
import com.quiz.quiz_exam.dto.StudentDtos.*;

import com.quiz.quiz_exam.exception.EntryNotfoundException;
import com.quiz.quiz_exam.service.StudentExamService;
import com.quiz.quiz_exam.util.StandResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student-exams")
@RequiredArgsConstructor
public class StudentExamController {

    private final StudentExamService studentExamService;
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/start")
    public ResponseEntity<StandResponseDto> startExam(@RequestBody StartExamRequest request) {
        return new  ResponseEntity<>(
                new StandResponseDto(
                        201,"Student Start the exam ",studentExamService.startExam(request)
                ), HttpStatus.CREATED
                );
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/submit")
    public ResponseEntity<StandResponseDto> submitExam(@RequestBody SubmitAnswersRequest request) {

        return new  ResponseEntity<>(
                new StandResponseDto(
                        201,"student submit the answer ",studentExamService.submitAnswer(request)
                ), HttpStatus.CREATED
        );
    }

     //Finish the exam
     @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/finish/{studentExamId}")
    public ResponseEntity<StandResponseDto> finishExam(
            @PathVariable Long studentExamId) {

         return new  ResponseEntity<>(
                 new StandResponseDto(
                         201,"student finished the exam ",studentExamService.finishExam(studentExamId)
                 ), HttpStatus.CREATED
         );
    }

    //Get  student results
    //@PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/result/{studentExamId}")
    public ResponseEntity<StandResponseDto> getResult(
            @PathVariable Long studentExamId) {
        return new  ResponseEntity<>(
                new StandResponseDto(
                        200,"student Exam Results",studentExamService.getStudentResult(studentExamId)
                ), HttpStatus.OK
        );
    }
    //List of student exams-Available ,pending
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/lists")
    public ResponseEntity<StandResponseDto> StudentsExamsLists( @RequestBody StudentRequestExamList studentRequestExamList
           ) {

        return new  ResponseEntity<>(
                new StandResponseDto(
                        200,"student Exam Lists",studentExamService.StudentExamLists(studentRequestExamList)
                ), HttpStatus.OK
        );
    }






}
