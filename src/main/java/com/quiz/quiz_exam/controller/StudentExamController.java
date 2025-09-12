package com.quiz.quiz_exam.controller;

import com.quiz.quiz_exam.dto.ExamDtos;
import com.quiz.quiz_exam.dto.ResultDtos;
import com.quiz.quiz_exam.dto.StudentDtos;
import com.quiz.quiz_exam.dto.StudentDtos.*;

import com.quiz.quiz_exam.exception.EntryNotfoundException;
import com.quiz.quiz_exam.security.JwtUtil;
import com.quiz.quiz_exam.service.StudentExamService;
import com.quiz.quiz_exam.util.StandResponseDto;
import jakarta.validation.Valid;
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
    private final JwtUtil jwtUtil;
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/start")
    public ResponseEntity<StandResponseDto> startExam(
            @RequestHeader("Authorization")String authHeader,
            @RequestBody StartExamRequest request){
             String token=authHeader.substring(7);
            Long studentId=jwtUtil.extractUserId(token);

        return new  ResponseEntity<>(
                new StandResponseDto(
                        200,"Student load the exam ",studentExamService.startExam(request.examId(),studentId)
                ), HttpStatus.OK
                );
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/submit")
    public ResponseEntity<StandResponseDto> submitExam(
            @RequestHeader("Authorization")String authHeader,
            @RequestBody SubmitAnswersRequest request) {
        String token=authHeader.substring(7);
        Long studentId=jwtUtil.extractUserId(token);

        return new  ResponseEntity<>(
                new StandResponseDto(
                        200,"student submit the answer ",studentExamService.submitAnswer(studentId,request)
                ), HttpStatus.CREATED
        );
    }

     //Finish the exam
     @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/finish")
    public ResponseEntity<StandResponseDto> finishExam(
            @RequestBody ExamDtos.Request req,
             @RequestHeader("Authorization")String authHeader){
         String token=authHeader.substring(7);
         Long studentId=jwtUtil.extractUserId(token);
         return new  ResponseEntity<>(
                 new StandResponseDto(
                         200,"student finished the exam ",studentExamService.finishExam(studentId,req)
                 ), HttpStatus.CREATED
         );
    }

    //Get  student results
    //@PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/result")
    public ResponseEntity<StandResponseDto> getResult(
            @RequestBody ResultDtos.ResultRequest req,
            @RequestHeader("Authorization")String authHeader){
        String token=authHeader.substring(7);
        Long userId=jwtUtil.extractUserId(token);
        String role=jwtUtil.extractUserRole(token);
        Object resultData;

        if(role.equals("TEACHER")){
            resultData=studentExamService.getStudentResult(req.studentExamId());
        }
        else if(role.equals("STUDENT")){
            resultData=studentExamService.getOwnResult(req.studentExamId(),userId);
        }
        else{
            throw new EntryNotfoundException("Unauthorized role");
        }

        return new  ResponseEntity<>(
                new StandResponseDto(
                        200,"student Exam Results",resultData
                ), HttpStatus.OK
        );
    }
    //List of student exams-Available ,pending
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/lists")
    public ResponseEntity<StandResponseDto> StudentsExamsLists( @RequestHeader("Authorization")String authHeader, @RequestBody StudentRequestExamList studentRequestExamList
           ) {
        String token=authHeader.substring(7);
        Long studentId=jwtUtil.extractUserId(token);

        return new  ResponseEntity<>(
                new StandResponseDto(
                        200,"student Exam Lists",studentExamService.StudentExamLists(studentId,studentRequestExamList)
                ), HttpStatus.OK
        );
    }






}
