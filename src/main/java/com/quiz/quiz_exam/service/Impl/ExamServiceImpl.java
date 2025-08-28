package com.quiz.quiz_exam.service.Impl;

import com.quiz.quiz_exam.dto.DashboardDto;
import com.quiz.quiz_exam.dto.ExamDtos;
import com.quiz.quiz_exam.dto.QuestionDtos;
import com.quiz.quiz_exam.dto.StudentDtos;
import com.quiz.quiz_exam.entity.Exam;
import com.quiz.quiz_exam.entity.Question;
import com.quiz.quiz_exam.enums.ExamStatus;
import com.quiz.quiz_exam.exception.EntryNotfoundException;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;



    @Transactional
    public ExamDtos.ExamResponse saveUpdateDeleteExam(Long teacherId, ExamDtos.CreateExamRequest req ) {
        if (req.isNew() || req.isUpdate()) {
            validExamDatetime(req);
        }

        if (req.isNew()) {
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
                    .orElseThrow(() -> new EntryNotfoundException("Exam not found after save")));
        } else if (req.isUpdate()) {
            //update the exam
            Exam exam = examRepository.findById(req.examId())
                    .orElseThrow(() -> new EntryNotfoundException("Exam not found with id" + req.examId()));
            if (exam.getStatus() == ExamStatus.PUBLISHED) {
                throw new IllegalArgumentException("Caonnot update a published exam");
            }
            exam.setTitle(req.title());
            exam.setDate(req.date());
            exam.setStartTime(req.startedTime());
            exam.setEndTime(req.endTime());
            //remove  old questions
            if (!exam.getQuestions().isEmpty()) {
                questionRepository.deleteAll(exam.getQuestions());
                exam.getQuestions().clear();

            }
            //Add new question
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
                    exam.getQuestions().add(qu);
                }

            }
            return toResponse(examRepository.save(exam));
        }else if(req.isRemove()){
            //delete the exam
            Exam exam =examRepository.findById(req.examId())
                    .orElseThrow(()->new EntryNotfoundException("exam not found with id"+req.examId()));

            //delete Question first
            if(!exam.getQuestions().isEmpty()){
                questionRepository.deleteAll(exam.getQuestions());
            }
            examRepository.delete(exam);

        }
        throw new IllegalArgumentException("At least one operation (isNew,isUpdate,isRemove) must be  true");
    }
    private void validExamDatetime(ExamDtos.CreateExamRequest req){
        LocalDateTime today= LocalDate.now().atStartOfDay();
        LocalDateTime now =LocalDateTime.now();
        //validate date
        if (req.date().isBefore(LocalDate.now().atStartOfDay())) {
            throw new IllegalArgumentException("Exam date cannot be in the past");
        }
        //validate start and end times
        if (!req.startedTime().isBefore(req.endTime())) {
            throw new IllegalArgumentException("start time must be before end time");
        }
        if(req.date().isEqual(today)&& req.startedTime().isBefore(now)){
            throw new IllegalArgumentException("Start time must be in the future");
        }
    }


    public Page<ExamDtos.ExamResponse> listPublished(ExamDtos.TeacherExamList teacherExamList) {
        Pageable pageable = PageRequest.of(teacherExamList.page(), teacherExamList.size());
        return examRepository.findPublishedExams(ExamStatus.PUBLISHED, teacherExamList.search(), pageable)
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

    private ExamDtos.ExamResponse toResponse(Exam e) {

        return new ExamDtos.ExamResponse(e.getTitle(),e.getLastUpdated(),e.getStatus());
    }

}
