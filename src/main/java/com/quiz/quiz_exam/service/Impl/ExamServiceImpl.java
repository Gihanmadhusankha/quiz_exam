package com.quiz.quiz_exam.service.Impl;

import com.quiz.quiz_exam.dto.ExamDtos;
import com.quiz.quiz_exam.entity.Exam;
import com.quiz.quiz_exam.entity.Question;
import com.quiz.quiz_exam.enums.ExamStatus;
import com.quiz.quiz_exam.repository.ExamRepository;
import com.quiz.quiz_exam.repository.QuestionRepository;
import com.quiz.quiz_exam.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;



    @Transactional
    public ExamDtos.ExamResponse createExam(Long teacherId, ExamDtos.CreateExamRequest req) {
        Exam exam = Exam.builder()
                .teacherId(teacherId)
                .title(req.title())
                .date(req.date())
                .startTime(req.startedTime())
                .endTime(req.endTime())
                .status(ExamStatus.DRAFT)
                .build();
        exam = examRepository.save(exam);

        if (req.questions() != null) {
            for (var q : req.questions()) {
                Question qu = Question.builder()
                        .exam(exam)
                        .questionText(q.questionText())
                        .option_a(q.optionA()).option_b(q.optionB()).option_c(q.optionC()).option_d(q.optionD())
                        .correctOption(q.correctOption())
                        .build();
                questionRepository.save(qu);
            }
        }
        return toResponse(examRepository.findById(exam.getExamId()).orElseThrow());
    }

    public ExamDtos.ExamResponse updateExam(Long examId, ExamDtos.CreateExamRequest req) {
        Exam exam = examRepository.findById(examId).orElseThrow();

        if (exam.getStatus() == ExamStatus.PUBLISHED) {
            throw new IllegalStateException("Cannot update a published exam");
        }

        exam.setTitle(req.title());
        exam.setDate(req.date());
        exam.setStartTime(req.startedTime());
        exam.setEndTime(req.endTime());

        // Clear old questions and save new ones
        questionRepository.deleteAll(exam.getQuestions());

        if (req.questions() != null) {
            for (var q : req.questions()) {
                Question qu = Question.builder()
                        .exam(exam)
                        .questionText(q.questionText())
                        .option_a(q.optionA())
                        .option_b(q.optionB())
                        .option_c(q.optionC())
                        .option_d(q.optionD())
                        .correctOption(q.correctOption())
                        .build();
                questionRepository.save(qu);
            }
        }

        return toResponse(examRepository.save(exam));
    }


    public List<ExamDtos.ExamResponse> listPublished() {
        return examRepository.findAll().stream()
                .filter(e -> e.getStatus() == ExamStatus.PUBLISHED)
                .map(this::toResponse).toList();
    }

    public void deleteExam(Long examId) {
        Exam exam = examRepository.findById(examId).orElseThrow();
        examRepository.delete(exam);
    }
    public List<ExamDtos.ExamResponse> listByTeacherExam(Long teacherId) {
        return examRepository.findByTeacherId(teacherId).stream()
                .map(this::toResponse).toList();
    }



    public ExamDtos.ExamResponse publish(Long examId) {
        Exam e = examRepository.findById(examId).orElseThrow();
        e.setStatus(ExamStatus.PUBLISHED);
        return toResponse(examRepository.save(e));
    }

    public ExamDtos.ExamResponse get(Long id) {
        return toResponse(examRepository.findById(id).orElseThrow());
    }
public List<Question>getQuestionByExamId(Long examId){
        return questionRepository.findByExam_ExamId(examId);
}
    private ExamDtos.ExamResponse toResponse(Exam e) {
        var qs = e.getQuestions().stream().map(q -> new ExamDtos.QuestionDto(
                q.getQuestionId(), q.getQuestionText(), q.getOption_a(), q.getOption_b(), q.getOption_c(), q.getOption_d(), q.getCorrectOption()
        )).toList();
        return new ExamDtos.ExamResponse(e.getExamId(), e.getTitle(), e.getDate(), e.getStartTime(), e.getEndTime(), e.getStatus(), qs);
    }
}
