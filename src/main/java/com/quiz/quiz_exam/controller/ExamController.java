package com.quiz.quiz_exam.controller;

import com.quiz.quiz_exam.dto.ExamDtos;
import com.quiz.quiz_exam.dto.StudentDtos;
import com.quiz.quiz_exam.service.ExamService;
import com.quiz.quiz_exam.service.StudentExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;
    private final StudentExamService studentExamService;

    //  Teacher creates exam
    @PostMapping("/create")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ExamDtos.ExamResponse> create(
            @RequestParam Long teacherId,
            @Valid @RequestBody ExamDtos.CreateExamRequest req) {
        return ResponseEntity.ok(examService.createExam(teacherId, req));
    }
    //Teacher update the exams
    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/update")
    public ExamDtos.ExamResponse updateExam(
            @RequestParam Long examId,
            @RequestBody ExamDtos.CreateExamRequest req) {
        return examService.updateExam(examId, req);
    }

    //  Teacher lists own exams (Draft + Published)
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/teacher")
    public ResponseEntity<Page<ExamDtos.ExamResponse>> listTeacherExams(@RequestBody ExamDtos.TeacherExamList teacherExamList) {
        return ResponseEntity.ok(examService.listByTeacherExam( teacherExamList ));
    }

    //  List published exams (for students)
    @GetMapping("/published")
    public ResponseEntity<Page<ExamDtos.ExamResponse>> listPublished(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(examService.listPublished(page, size, search));
    }



    //  Teacher publishes exam
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/{id}/publish")
    public ResponseEntity<ExamDtos.ExamResponse> publish(@PathVariable Long id) {
        return ResponseEntity.ok(examService.publish(id));
    }

    //  Get one exam (with questions)
    @GetMapping("/{id}")
    public ResponseEntity<ExamDtos.ExamResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(examService.get(id));
    }

    // Teacher delete the exam
    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        examService.deleteExam(id);
        return ResponseEntity.noContent().build();
    }

}
