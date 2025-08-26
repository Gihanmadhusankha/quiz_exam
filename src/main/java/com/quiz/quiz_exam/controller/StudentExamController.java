package com.quiz.quiz_exam.controller;

import com.quiz.quiz_exam.dto.ResultDtos;
import com.quiz.quiz_exam.dto.StudentDtos;
import com.quiz.quiz_exam.dto.StudentDtos.*;

import com.quiz.quiz_exam.service.StudentExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<StudentExamResponse> startExam(@RequestBody StartExamRequest request) {
        return ResponseEntity.ok(studentExamService.startExam(request));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/submit")
    public ResponseEntity<AnswerDto> submitExam(@RequestBody SubmitAnswersRequest request) {
        return ResponseEntity.ok(studentExamService.submitAnswer(request));
    }

     //Finish the exam
     @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/finish/{studentExamId}")
    public ResponseEntity<ResultDtos.StudentExamSummary> finishExam(
            @PathVariable Long studentExamId) {
        return ResponseEntity.ok(studentExamService.finishExam(studentExamId));
    }

    //Get  student results
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/result/{studentExamId}")
    public ResponseEntity<ResultDtos.StudentResultRow> getResult(
            @PathVariable Long studentExamId) {
        return ResponseEntity.ok(studentExamService.getStudentResult(studentExamId));
    }
    //List of student exams-Available ,pending
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/lists")
    public ResponseEntity<Page<StudentExamList>> listStudentsExams( @RequestBody StudentRequestExamList studentRequestExamList
           ) {

        return ResponseEntity.ok(studentExamService.StudentExamLists(studentRequestExamList));
    }






}
