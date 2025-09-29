package com.quiz.quiz_exam.service.Impl;
import com.quiz.quiz_exam.dto.ExamDtos;
import com.quiz.quiz_exam.dto.QuestionDtos;
import com.quiz.quiz_exam.dto.ResultDtos;
import com.quiz.quiz_exam.dto.StudentDtos;
import com.quiz.quiz_exam.entity.*;
import com.quiz.quiz_exam.enums.ExamStatus;
import com.quiz.quiz_exam.enums.RecordStatus;
import com.quiz.quiz_exam.enums.StudentExamStatus;
import com.quiz.quiz_exam.exception.*;
import com.quiz.quiz_exam.repository.*;
import com.quiz.quiz_exam.service.StudentExamService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.quiz.quiz_exam.enums.StudentExamStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentExamServiceImpl implements StudentExamService {

    private final StudentExamRepository studentExamRepository;
    private final ExamRepository examRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final StudentAnswerRepository studentAnswerRepository;

    @Transactional

    // Student starts the exam
    public StudentDtos.StartExamResponse startExam(Long examId, Long studentId) {
        // Get Exam
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new EntryNotfoundException("Exam not found"));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new EntryNotfoundException("Student not found"));

        //check if exam already started for this student
        StudentExam studentExam =  studentExamRepository
                .findByStudentAndExam(studentId, examId)
                .orElse(null);

        if (studentExam == null) {
            User stu = userRepository.findById(studentId)
                    .orElseThrow(() -> new EntryNotfoundException("Student not found"));

            studentExam = StudentExam.builder()
                    .student(stu)
                    .exam(exam)
                    .studentExamStatus(NEW)
                    .status(RecordStatus.ONLINE)
                    .build();

            studentExam = studentExamRepository.save(studentExam);
        }




        //check the Started time

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(exam.getStartTime())) {
            throw new InvalidTimeException("Exam has not started yet");
        }
        if (now.isAfter(exam.getEndTime())) {
            throw new InvalidTimeException("Exam is already finished");
        }

        //check whether exam is ended or not
        if (exam.getExamStatus() == ExamStatus.ENDED) {
            throw new ExamTimeOverException("Exam has ended");
        }
        //check whether exam is published or not
        if (exam.getExamStatus() != ExamStatus.PUBLISHED) {
            throw new ExamNotPublishedException("Exam is not published yet");
        }
        //if student already attended ,block the  re-start
        if (studentExam.getStudentExamStatus() == StudentExamStatus.ATTENDED) {
            throw new ExamAlreadyPublishedCompletedException("You have already completed this exam");


        }
        // Convert Exam.Questions → DTO
        List<QuestionDtos.Question> questionDtos = exam.getQuestions()
                .stream()
                .map(q -> new QuestionDtos.Question(q.getQuestionId(), q.getQuestionText(), q.getOptionA(), q.getOptionB(), q
                        .getOptionC(), q.getOptionD())
                ).collect(Collectors.toList());

        Long lastAnsweredQuestionId = studentAnswerRepository
                .findLastAnsweredQuestionId(studentExam.getStudentExamId())
                .stream()
                .findFirst()
                .orElse(null);



        return new StudentDtos.StartExamResponse(exam.getExamId(), studentExam.getStudentExamId(), exam.getTitle(),exam.getEndTime(), questionDtos,lastAnsweredQuestionId);
    }


    @Transactional
    // Submit a single answer
    public StudentDtos.AnswerDto submitAnswer(Long studentId, StudentDtos.SubmitAnswersRequest request) {

        // Get Student (User)
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new EntryNotfoundException("Student not found"));

        // 2. Find active StudentExam for this student + exam
        StudentExam studentExam = (StudentExam) studentExamRepository
                .findByStudentAndExam(studentId, request.examId())
                .orElseThrow(() -> new EntryNotfoundException("StudentExam not found for studentId="
                        + studentId + " examId=" + request.examId()));

        if (studentExam.getStudentExamStatus() == ATTENDED) {
            throw new ExamAlreadyPublishedCompletedException("Exam already completed");
        }
        //Time validation
        LocalDateTime now = LocalDateTime.now();
        Exam exam = studentExam.getExam();


        if (exam.getExamStatus() == ExamStatus.ENDED || now.isAfter(exam.getEndTime())) {
            //Auto mark exam as completed
            studentExam.setStudentExamStatus(ATTENDED);
            studentExamRepository.save(studentExam);
            throw new ExamTimeOverException("Exam has ended .Auto-completed");
        }

        Question question = questionRepository.findById(request.questionId())
                .orElseThrow(() -> new EntryNotfoundException("Question not found"));

        Optional<StudentAnswer> existing = studentAnswerRepository
                .findByStudentExamAndQuestion(studentExam.getStudentExamId(), question.getQuestionId());

        StudentAnswer answerEntity;
        if (existing.isPresent()) {
            answerEntity = existing.get();
            answerEntity.setSelected_option(request.selectedOption());
        } else {
            answerEntity = new StudentAnswer();
            answerEntity.setStudentExam(studentExam);
            answerEntity.setQuestion(question);
            answerEntity.setSelected_option(request.selectedOption());
        }

        StudentAnswer saved = studentAnswerRepository.save(answerEntity);
        studentExam.setStudentExamStatus(PENDING);

        return new StudentDtos.AnswerDto(saved.getStudentExam().getExam().getExamId(), saved.getStudentExam().getStudentExamId(), saved.getQuestion().getQuestionId(), saved.getSelected_option());
    }

    //student complete the exam
    @Transactional
    public ResultDtos.StudentExamSummary finishExam(Long studentId, ExamDtos.Request req) {
        StudentExam se = studentExamRepository.findByExamIdAndStudentId(req.examId(), studentId)
                .orElseThrow(() -> new EntryNotfoundException("StudentExam not found"));

        Exam exam = se.getExam();

        //check if exam is ended based on current time
        boolean examEnded = exam.getExamStatus() == ExamStatus.ENDED || req.isTimerEnd();
        if (examEnded) {
            // Auto-complete student exam
            se.setStudentExamStatus(ATTENDED);
            studentExamRepository.save(se);

            long totalQuestions = exam.getQuestions().size();
            long answeredQuestions = se.getStudentAnswers().size();
            long correctCount = studentAnswerRepository.countCorrectAnswers(se);
            double percentage = totalQuestions == 0 ? 0 : (correctCount * 100.0 / totalQuestions);

            return new ResultDtos.StudentExamSummary(
                    se.getStudentExamId(),
                    exam.getExamId(),
                    se.getStudentExamStatus(),
                    totalQuestions,
                    correctCount,
                    percentage
            );
        }

        long totalQuestions = se.getExam().getQuestions().size();
        long answeredQuestion = se.getStudentAnswers().size();

        if (answeredQuestion < totalQuestions) {
            throw new IncompleteExamException("student has not answered all questions");
        }
        long correctCount = studentAnswerRepository.countCorrectAnswers(se);
        double percentage = totalQuestions == 0 ? 0 : (correctCount * 100.0 / totalQuestions);

        se.setStudentExamStatus(ATTENDED);
        studentExamRepository.save(se);

        return new ResultDtos.StudentExamSummary(
                se.getStudentExamId(),
                se.getExam().getExamId(),
                se.getStudentExamStatus(),
                totalQuestions,
                correctCount,
                percentage
        );
    }

    @Override
    public ResultDtos.StudentResultRow getStudentResult(Long studentExamId) {
        StudentExam se = studentExamRepository.findById(studentExamId)
                .orElseThrow(() -> new EntryNotfoundException("Result  not found"));
        return getResult(se);
    }

    public ResultDtos.StudentResultRow getOwnResult(Long studentExamId, Long userId) {
        StudentExam se = studentExamRepository.findById(studentExamId)
                .orElseThrow(() -> new EntryNotfoundException("Result  not found"));

        if (!se.getStudent().getUserId().equals(userId)) {
            throw new UnauthorizedAccessException("You are not allowed to view another student's result");
        }
        return getResult(se);
    }


    //  Get student result

    private ResultDtos.StudentResultRow getResult(StudentExam se) {

        // All questions of this exam
        List<Question> questions = se.getExam().getQuestions();
        int totalQuestions = questions.size();

        // Fetch all answers
        List<StudentAnswer> answers = studentAnswerRepository.findByStudentExamId(se.getStudentExamId());
        Map<Long, StudentAnswer> answerMap = answers.stream()
                .collect(Collectors.toMap(a -> a.getQuestion().getQuestionId(), a -> a));

        long correctCount = 0;
        List<ResultDtos.QuestionResult> questionResults = new ArrayList<>();

        for (Question q : questions) {
            StudentAnswer ans = answerMap.get(q.getQuestionId());

            boolean correct = false;
            if (ans != null) {
                // Compare student's selected answer with correct option
                correct = q.getCorrectOption().equalsIgnoreCase(ans.getSelected_option());
            }

            if (correct) {
                correctCount++;
            }
            String status;
            if (ans == null) {
                status = "Unanswered";
            } else if (correct) {
                status = "Correct";

            } else {
                status = "Wrong";
            }

            questionResults.add(new ResultDtos.QuestionResult(
                    q.getQuestionId(),
                    q.getQuestionText(),
                    correct,
                    status
                    //correct ? "Correct" : "Wrong"
            ));
        }

        // Scale to 100 points
        double percentage = totalQuestions == 0 ? 0 : (correctCount * 100.0 / totalQuestions);
        int obtainedPoints = (int) Math.round(percentage);
        int totalPoints = 100; // always 100

        String grade = gradeFor(percentage);
        String passFail = percentage >= 60 ? "Passed" : "Failed";

        return new ResultDtos.StudentResultRow(
                se.getExam().getTitle(),
                se.getStudentExamId(),
                se.getStudent().getUserId(),
                se.getStudent().getName(),
                se.getStudentExamStatus(),
                totalQuestions,
                correctCount,
                percentage,
                obtainedPoints,
                totalPoints,
                grade,
                passFail,
                questionResults
        );
    }

    @Transactional
    @Override
    public Page<StudentDtos.StudentExamList> StudentExamLists(
            Long studentId,
            StudentDtos.StudentRequestExamList request
    ) {
        // Validate student exists
        userRepository.findById(studentId)
                .orElseThrow(() -> new EntryNotfoundException("Student not found with id " + studentId));

        // Create pageable object
        Pageable pageable = PageRequest.of(request.page(), request.size());
        Page<Exam> exams;
        if (request.search() == null || request.search().isBlank()) {
            exams = examRepository.findNotDraftExams(
                    pageable
            );
        } else {
            String searchTerm = request.search().trim();
            exams = examRepository.findNotDraftExamsAndSearch(
                    searchTerm,
                    pageable
            );
        }

        return exams.map(e->toStudentResponse(e,studentId));
    }

    private StudentDtos.StudentExamList toStudentResponse(Exam e, Long studentId ) {

        long durationMinutes = Duration.between(e.getStartTime(), e.getEndTime()).toMinutes();
        long hours = durationMinutes / 60;
        long minutes = durationMinutes % 60;
        String duration = String.format("%02dh %02dmin", hours, minutes);
        Optional<StudentExam> se = studentExamRepository.findByExamIdAndStudentId(e.getExamId(),studentId);
        String status;

         Long studentExamId=se.map(StudentExam::getStudentExamId).orElse(null);

        //check if exam is already ended based on current time
        if (e.getExamStatus() == ExamStatus.ENDED || LocalDateTime.now().isAfter(e.getEndTime())) {
            status = "ENDED";
        }  else {
            status = se.map(s -> switch (s.getStudentExamStatus()) {
                case PENDING -> "PENDING";
                case ATTENDED -> "ATTENDED";
                default -> "NEW";
            }).orElse("NEW");
        }


        return new StudentDtos.StudentExamList(

                e.getExamId(),
                studentExamId,
                e.getTitle(),
                e.getStartTime(),
                duration,
                status
        );
    }

    private String gradeFor(double pct) {
        if (pct >= 85) return "A";
        if (pct >= 65) return "B";
        if (pct >= 55) return "C";
        if (pct >= 35) return "D";
        return "F";
    }




}