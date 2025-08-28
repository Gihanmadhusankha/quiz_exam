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
public class ExamController {

    private final ExamService examService;
    private final JwtUtil jwtUtil;

    //  Teacher creates or update or delete  exam
    @PostMapping("/create")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<StandResponseDto> createUpdateDeleteExam(
            @RequestHeader("Authorization")String authHeader,
            @Valid @RequestBody ExamDtos.CreateExamRequest req) {
        String token=authHeader.substring(7);
        Long teacherId=jwtUtil.extractUserId(token);
        if(req.isRemove()){
            return new ResponseEntity<>(
                    new StandResponseDto( 204,"examDeleted successfully",null),
            HttpStatus.NO_CONTENT
                    );
        }

        return new ResponseEntity<>(
                new StandResponseDto(
                        200,"Exam Created or updated   Successfully",examService.saveUpdateDeleteExam(teacherId, req)
                ),
                HttpStatus.OK
        );

    }


    //  Teacher lists own exams (Draft + Published)
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/teacher")
    public ResponseEntity<StandResponseDto> listTeacherExams(@RequestBody ExamDtos.TeacherExamList teacherExamList) {
        return new ResponseEntity<>(
                new StandResponseDto(
                        200,"Teacher Exam Lists",examService.listByTeacherExam( teacherExamList )
                ),
                HttpStatus.OK
        );
    }

    //  List published exams (for students)
    @GetMapping("/published")
    public ResponseEntity<StandResponseDto> listPublished(@RequestBody ExamDtos.TeacherExamList teacherExamList
           ) {

        return new ResponseEntity<>(
                new StandResponseDto(
                        200,"Teacher  Published Exam Lists",examService.listPublished(teacherExamList )
                ),
                HttpStatus.OK
        );
    }



    //  Teacher publishes exam
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/{id}/publish")
    public ResponseEntity<StandResponseDto> publish(@PathVariable Long id) {

        return new ResponseEntity<>(
                new StandResponseDto(
                        200,"Teacher  Published The Exam Successfully",examService.publish(id)
                ),
                HttpStatus.OK
        );
    }

    //  Get one exam (with questions)
    @GetMapping("/{id}")
    public ResponseEntity<StandResponseDto> get(@PathVariable Long id) {

        return new ResponseEntity<>(
                new StandResponseDto(
                        200,"Student exams",examService.get(id)
                ),HttpStatus.OK
        );
    }



}
