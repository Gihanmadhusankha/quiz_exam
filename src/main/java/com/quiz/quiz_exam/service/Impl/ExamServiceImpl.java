package com.quiz.quiz_exam.service.Impl;

import com.quiz.quiz_exam.dto.ExamDtos;
import com.quiz.quiz_exam.entity.Exam;
import com.quiz.quiz_exam.entity.Question;
import com.quiz.quiz_exam.enums.ExamStatus;
import com.quiz.quiz_exam.enums.RecordStatus;
import com.quiz.quiz_exam.exception.EntryNotfoundException;
import com.quiz.quiz_exam.exception.ExamAlreadyPublishedCompletedException;
import com.quiz.quiz_exam.exception.InvalidTimeException;
import com.quiz.quiz_exam.exception.UnauthorizedAccessException;
import com.quiz.quiz_exam.repository.ExamRepository;
import com.quiz.quiz_exam.repository.QuestionRepository;
import com.quiz.quiz_exam.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
                    .examStatus(ExamStatus.DRAFT)
                    .status(RecordStatus.ONLINE)
                    .build();
            exam = examRepository.save(exam);

            if (req.questions() != null) {
                for (var q : req.questions()) {
                    if (q.isNew()) {
                        Question qu = Question.builder()
                                .exam(exam)
                                .questionText(q.questionText())
                                .optionA(q.optionA())
                                .optionB(q.optionB())
                                .optionC(q.optionC())
                                .optionD(q.optionD())
                                .correctOption(q.correctOption())
                                .status(RecordStatus.ONLINE)
                                .build();
                        exam.getQuestions().add(qu);
                    }
                }

            }

            return toResponse(examRepository.findById(exam.getExamId())
                    .orElseThrow(() -> new EntryNotfoundException("Exam not found after save")));
        } else if (req.isUpdate()) {
            //update the exam
            Exam exam = examRepository.findById(req.examId())
                    .orElseThrow(() -> new EntryNotfoundException("Exam not found with id" + req.examId()));
            if (exam.getExamStatus() == ExamStatus.PUBLISHED) {
                throw new IllegalArgumentException("Cannot update a published exam");
            }
            exam.setTitle(req.title());
            exam.setDate(req.date());
            exam.setStartTime(req.startedTime());
            exam.setEndTime(req.endTime());


            //Add new question
            if (req.questions() != null) {
                for (var q : req.questions()) {
                    if (q.isNew()) {
                        // add question
                        Question newQ = Question.builder()
                                .exam(exam)
                                .questionText(q.questionText())
                                .optionA(q.optionA())
                                .optionB(q.optionB())
                                .optionC(q.optionC())
                                .optionD(q.optionD())
                                .correctOption(q.correctOption())
                                .status(RecordStatus.ONLINE)
                                .build();
                        exam.getQuestions().add(newQ);
                    } else if (q.isUpdate()) {
                        //question update
                        Question existing = questionRepository.findById(q.questionId())
                                .orElseThrow(() -> new EntryNotfoundException("question not found"));
                        existing.setQuestionText(q.questionText());
                        existing.setOptionA(q.optionA());
                        existing.setOptionB(q.optionB());
                        existing.setOptionC(q.optionC());
                        existing.setOptionD(q.optionD());
                        existing.setCorrectOption(q.correctOption());
                        existing.setStatus(RecordStatus.ONLINE);

                        questionRepository.save(existing);
                    } else if (q.isRemove()) {
                        //question delete

                        Question toRemove = questionRepository.findById(q.questionId())
                                .orElseThrow(() -> new EntryNotfoundException("Question not found"));


                        toRemove.setStatus(RecordStatus.OFFLINE);
                        questionRepository.save(toRemove);
                    }
                }
            }
            return toResponse(examRepository.save(exam));
        } else if (req.isRemove()) {
            //delete the exam
            Exam exam = examRepository.findById(req.examId())
                    .orElseThrow(() -> new EntryNotfoundException("exam not found with id" + req.examId()));

            exam.setStatus(RecordStatus.OFFLINE);
            if (!exam.getQuestions().isEmpty()) {
                for (Question q : exam.getQuestions()) {
                    q.setStatus(RecordStatus.OFFLINE);
                    questionRepository.save(q);
                }
            }
            return toResponse(examRepository.save(exam));
        }
         throw new IllegalArgumentException("Invalid request type");

    }

    private void validExamDatetime(ExamDtos.CreateExamRequest req){
        LocalDateTime today= LocalDate.now().atStartOfDay();
        LocalDateTime now =LocalDateTime.now();
        //validate date
        if (req.date().isBefore(LocalDate.now().atStartOfDay())) {
            throw new InvalidTimeException("Exam date cannot be in the past");
        }
        // Validate start time is in the future
        if (req.startedTime().isBefore(now)) {
            throw new InvalidTimeException("Start time must be in the future");
        }

        //validate start and end times
        if (!req.startedTime().isBefore(req.endTime())) {
            throw new InvalidTimeException("start time must be before end time");
        }
        if(req.date().isEqual(today)&& req.startedTime().isBefore(now)){
            throw new InvalidTimeException("Start time must be in the future");
        }
    }


    @Override
    public ExamDtos.loadExamResponse loadExam(Long examId, Long teacherId) {

         Exam exam= examRepository.findById(examId)
                 .orElseThrow(()-> new EntryNotfoundException("Exam not found"));
            if(!exam.getTeacherId().equals(teacherId)){
                throw new UnauthorizedAccessException("Teacher not authorized to access this exam");
            }
            return new ExamDtos.loadExamResponse(
                    exam.getExamId(),
                    exam.getTitle(),
                    exam.getDate(),
                    exam.getStartTime(),
                    exam.getEndTime(),
                    exam.getQuestions().stream()
                            .filter(q->q.getStatus()==RecordStatus.ONLINE)
                            .map(q->new ExamDtos.QuestionDto(
                                    q.getQuestionId(),
                                    q.getQuestionText(),
                                    q.getOptionA(),
                                    q.getOptionB(),
                                    q.getOptionC(),
                                    q.getOptionD(),
                                    q.getCorrectOption()
                            )).toList()
            );
    }

    public Page<ExamDtos.ExamResponse> listPublished(ExamDtos.TeacherExamList teacherExamList) {
        Pageable pageable = PageRequest.of(teacherExamList.page(), teacherExamList.size());

        return examRepository.findPublishedExams(ExamStatus.PUBLISHED, teacherExamList.search(), pageable)
                .map(this::toResponse);
    }

    @Override
    public Page<ExamDtos.ExamResponse> listByTeacherExam(Long teacherId,ExamDtos.TeacherExamList teacherExamList) {
        Pageable pageable = PageRequest.of(teacherExamList.page(), teacherExamList.size());
        if (teacherExamList.search() == null || teacherExamList.search().isBlank() || teacherExamList.search().isEmpty()){
            return examRepository.findByTeacherId(teacherId, pageable)
                    .map(this::toResponse);
        }else{
            return examRepository.findByTeacherIdAndSearch(teacherId, teacherExamList.search(), pageable)
                    .map(this::toResponse);
        }
    }
    @Transactional
    public ExamDtos.ExamResponse publish(Long examId) {
        Exam e = examRepository.findById(examId).orElseThrow();
        if(e.getExamStatus()==ExamStatus.PUBLISHED) {
            throw new ExamAlreadyPublishedCompletedException("exam is already published");
        }

        if(e.getExamStatus()==ExamStatus.DRAFT) {
            e.setExamStatus(ExamStatus.PUBLISHED);
        }
        return toResponse(examRepository.save(e));


    }



    private ExamDtos.ExamResponse toResponse(Exam e) {

        return new ExamDtos.ExamResponse(e.getTitle(),e.getLastUpdated(),e.getExamStatus());
    }

}
