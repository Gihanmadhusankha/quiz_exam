package com.quiz.quiz_exam.service.Impl;

import com.quiz.quiz_exam.dto.StudentDtos;
import com.quiz.quiz_exam.dto.StudentDtos.*;
import com.quiz.quiz_exam.entity.*;
import com.quiz.quiz_exam.enums.StudentExamStatus;
import com.quiz.quiz_exam.repository.*;

import com.quiz.quiz_exam.service.StudentExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class  StudentExamServiceImpl implements StudentExamService {

    private final StudentExamRepository studentExamRepository;
    private final ExamRepository examRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final StudentAnswerRepository studentAnswerRepository;

    //student start the exam
    public StudentExamResponse startExam(StartExamRequest request) {
        Exam exam = examRepository.findById(request.examId())
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        User student = userRepository.findById(request.studentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        StudentExam studentExam = new StudentExam();
        studentExam.setExam(exam);
        studentExam.setStudent(student);
        studentExam.setStatus(StudentExamStatus.PENDING);

        StudentExam saved = studentExamRepository.save(studentExam);

        return new StudentExamResponse(
                saved.getStudentExamId(),
                exam.getExamId(),
                saved.getStatus(),
                exam.getQuestions().size(),
                0,
                List.of()
        );
    }

    //student submit the exam

    public StudentExamResponse submitExam(SubmitAnswersRequest request) {
        StudentExam studentExam = studentExamRepository.findById(request.studentExamId())
                .orElseThrow(() -> new RuntimeException("StudentExam not found"));

        List<AnswerDto> answers = request.answers();
        int correctCount = 0;

        for (AnswerDto ans : answers) {
            Question question = questionRepository.findById(ans.questionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            StudentAnswer sa = new StudentAnswer();
            sa.setStudentExam(studentExam);
            sa.setQuestion(question);
            sa.setSelected_option(ans.selectedOption());
            studentAnswerRepository.save(sa);

            if (question.getCorrectOption().equalsIgnoreCase(ans.selectedOption())){
                correctCount++;
            }
        }

        studentExam.setStatus(StudentExamStatus.ATTENDED);
        studentExamRepository.save(studentExam);

        return new StudentExamResponse(
                studentExam.getStudentExamId(),
                studentExam.getExam().getExamId(),
                studentExam.getStatus(),
                studentExam.getExam().getQuestions().size(),
                correctCount,
                answers
        );
    }

    @Override
    public StudentExamResponse getStudentExam(Long studentExamId) {
        StudentExam studentExam = studentExamRepository.findById(studentExamId)
                .orElseThrow(() -> new RuntimeException("Student exam not found"));

        List<StudentDtos.AnswerDto> answers = studentExam.getStudentAnswers().stream()
                .map(sa -> new StudentDtos.AnswerDto(sa.getQuestion().getQuestionId(), sa.getSelected_option()))
                .collect(Collectors.toList());

        long correctCount = studentExam.getStudentAnswers().stream()
                .filter(sa -> sa.getQuestion().getCorrectOption().equals(sa.getSelected_option()))
                .count();

        return new StudentDtos.StudentExamResponse(
                studentExam.getStudentExamId(),
                studentExam.getExam().getExamId(),
                studentExam.getStatus(),
                studentExam.getExam().getQuestions().size(),
                (int) correctCount,
                answers
        );
    }

    @Override
    public Page<StudentExamResponse> listStudentExams(Long studentId, int page, int size, String search) {

        Pageable pageable = PageRequest.of(page, size);
        return studentExamRepository.findByStudentIdAndSearch(studentId, search, pageable)
                .map(se -> getStudentExam(se.getStudentExamId()));
    }


}
