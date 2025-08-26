package com.quiz.quiz_exam.service.Impl;

import com.quiz.quiz_exam.dto.DashboardDto;
import com.quiz.quiz_exam.dto.ExamDtos;
import com.quiz.quiz_exam.dto.StudentDtos;
import com.quiz.quiz_exam.entity.Exam;
import com.quiz.quiz_exam.entity.Question;
import com.quiz.quiz_exam.enums.ExamStatus;
import com.quiz.quiz_exam.repository.ExamRepository;
import com.quiz.quiz_exam.repository.QuestionRepository;
import com.quiz.quiz_exam.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
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
            List<Question> questionList = new ArrayList<>();
            for (var q : req.questions()) {
                Question qu = Question.builder()
                        .exam(exam)
                        .questionText(q.questionText())
                        .optionA(q.optionA())
                        .optionB(q.optionB())
                        .optionC(q.optionC())
                        .optionD(q.optionD())
                        .correctOption(q.correctOption())
                        .build();
                questionRepository.save(qu);
                questionList.add(qu);
            }
            exam.setQuestions(questionList);
        }

        return toResponse(examRepository.findById(exam.getExamId())
                .orElseThrow(() -> new RuntimeException("Exam not found after save")));
    }

    public void deleteExam(Long examId) {
        Exam exam = examRepository.findById(examId).orElseThrow(()-> new RuntimeException("Exam not found"));
        examRepository.delete(exam);
    }
    public Page<ExamDtos.ExamResponse> listPublished(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        return examRepository.findPublishedExams(ExamStatus.PUBLISHED, search, pageable)
                .map(this::toResponse);
    }

    @Override
    public Page<ExamDtos.ExamResponse> listByTeacherExam(ExamDtos.TeacherExamList teacherExamList) {
        Pageable pageable = PageRequest.of(teacherExamList.page(), teacherExamList.size());
        if (teacherExamList.search() == null || teacherExamList.search().isBlank() || teacherExamList.search().isEmpty()){
            return examRepository.findByTeacherId(teacherExamList.teacherId(), pageable)
                    .map(this::toResponse);
        }else{
            return examRepository.findByTeacherIdAndSearch(teacherExamList.teacherId(), teacherExamList.search(), pageable)
                    .map(this::toResponse);
        }
    }







    public ExamDtos.ExamResponse publish(Long examId) {
        Exam e = examRepository.findById(examId).orElseThrow();
        e.setStatus(ExamStatus.PUBLISHED);
        return toResponse(examRepository.save(e));
    }

    public ExamDtos.ExamResponse get(Long id) {
        return toResponse(examRepository.findById(id).orElseThrow());
    }

    @Override
    public ExamDtos.ExamResponse updateExam(Long examId, ExamDtos.CreateExamRequest req) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found with id " + examId));

        if (exam.getStatus() == ExamStatus.PUBLISHED) {
            throw new IllegalStateException("Cannot update a published exam");
        }

        exam.setTitle(req.title());
        exam.setDate(req.date());
        exam.setStartTime(req.startedTime());
        exam.setEndTime(req.endTime());

        // Clear old quest
        exam.getQuestions().clear();

        // Add new questions
        if (req.questions() != null) {
            for (var q : req.questions()) {
                Question qu = Question.builder()
                        .exam(exam)
                        .questionText(q.questionText())
                        .optionA(q.optionA())
                        .optionB(q.optionB())
                        .optionC(q.optionC())
                        .optionD(q.optionD())
                        .correctOption(q.correctOption())
                        .build();

                exam.getQuestions().add(qu);
            }
        }

        return toResponse(examRepository.save(exam));
    }




    private ExamDtos.ExamResponse toResponse(Exam e) {
        var qs = e.getQuestions().stream().map(q -> new ExamDtos.QuestionDto(
                q.getQuestionId(), q.getQuestionText(), q.getOptionA(), q.getOptionB(), q.getOptionC(), q.getOptionD(), q.getCorrectOption()
        )).toList();
        return new ExamDtos.ExamResponse(e.getExamId(), e.getTitle(), e.getDate(), e.getStartTime(), e.getEndTime(), e.getStatus(), qs);
    }

}
