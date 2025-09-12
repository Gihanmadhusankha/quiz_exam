package com.quiz.quiz_exam.controller;

import com.quiz.quiz_exam.dto.ExamDtos;
import com.quiz.quiz_exam.dto.StudentDtos;
import com.quiz.quiz_exam.security.JwtUtil;
import com.quiz.quiz_exam.service.ExamService;
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
@RequestMapping("/api/exams")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ExamController {

    private final ExamService examService;
    private final JwtUtil jwtUtil;

    //  Teacher creates or update or delete  exam
    @PostMapping("/manage")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<StandResponseDto> createUpdateDeleteExam(
            @RequestHeader("Authorization")String authHeader,
            @Valid @RequestBody ExamDtos.CreateExamRequest req) {
        String token=authHeader.substring(7);
        Long teacherId=jwtUtil.extractUserId(token);
        if(req.isRemove()){
            examService.saveUpdateDeleteExam(teacherId, req);
            return new ResponseEntity<>(
                    new StandResponseDto( 204,"examDeleted successfully",null),
            HttpStatus.NO_CONTENT
                    );
        }

        return new ResponseEntity<>(
                new StandResponseDto(
                        200,"Exam Created or updated  Successfully",examService.saveUpdateDeleteExam(teacherId, req)
                ),
                HttpStatus.OK
        );

    }
    @PostMapping("/load")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<StandResponseDto> loadExam(
            @RequestHeader("Authorization")String authHeader,
            @RequestBody ExamDtos.loadExamRequest request){
        String token=authHeader.substring(7);
        Long teacherId=jwtUtil.extractUserId(token);

        return new  ResponseEntity<>(
                new StandResponseDto(
                        200,"Student load the exam ",examService.loadExam(request.examId(),teacherId)
                ), HttpStatus.OK
        );
    }


    //  Teacher lists own exams (Draft + Published)
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/teacher")
    public ResponseEntity<StandResponseDto> listTeacherExams(
            @RequestHeader("Authorization")String authHeader,
            @RequestBody ExamDtos.TeacherExamList teacherExamList) {
        String token=authHeader.substring(7);
        Long teacherId=jwtUtil.extractUserId(token);
        return new ResponseEntity<>(
                new StandResponseDto(
                        200,"Teacher Exam Lists",examService.listByTeacherExam(  teacherId,teacherExamList )
                ),
                HttpStatus.OK
        );
    }

    //  List published exams (for students)
    @PostMapping("/published")
    public ResponseEntity<StandResponseDto> listPublished(@RequestBody ExamDtos.TeacherExamList teacherExamList
           ) {

        return new ResponseEntity<>(
                new StandResponseDto(
                        200,"Teacher  Published Exam Lists",examService.listPublished(teacherExamList )
                ),
                HttpStatus.OK
        );
    }



    //  Teacher publish the exam
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/publish")
    public ResponseEntity<StandResponseDto> publish(@RequestBody ExamDtos.Request req) {

        return new ResponseEntity<>(
                new StandResponseDto(
                        200,"Teacher  Published The Exam Successfully",examService.publish(req.examId())
                ),
                HttpStatus.OK
        );
    }






}
