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

import java.nio.file.AccessDeniedException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

        // Get Student (User)
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new EntryNotfoundException("Student not found"));


        //check the Started time

        LocalDateTime now =LocalDateTime.now();
        if(now.isBefore(exam.getStartTime())){
            throw new InvalidTimeException("Exam has not started yet");
        }
        if(now.isAfter(exam.getEndTime())){
            throw new InvalidTimeException("Exam is already finished");
        }

        //check whether exam is ended or not
       if(exam.getExamStatus()== ExamStatus.ENDED){
           throw new ExamTimeOverException("Exam has ended");
       }
        //check whether exam is published or not
        if(exam.getExamStatus()!= ExamStatus.PUBLISHED){
            throw new ExamNotPublishedException("Exam is not published yet");
        }
       //check if exam already started for this student
        StudentExam studentExam = (StudentExam) studentExamRepository
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


        // Calculate remaining time (based on exam end time)
      /*  long timeLeftSeconds = Duration.between(now, exam.getEndTime()).toSeconds();
        if (timeLeftSeconds <= 0) {
            studentExam.setStudentExamStatus(ATTENDED);
            studentExamRepository.save(studentExam);
            timeLeftSeconds = 0;
        }
        long hours = timeLeftSeconds / 3600;
        long minutes = (timeLeftSeconds % 3600) / 60;
        long seconds = timeLeftSeconds % 60;
*/

       // String formattedDuration = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        // Convert Exam.Questions → DTO
        List<QuestionDtos.Question> questionDtos= exam.getQuestions()
                .stream()
                .map(q -> new QuestionDtos.Question(q.getQuestionId(), q.getQuestionText(),q.getOptionA(),q.getOptionB(),q
                        .getOptionC(),q.getOptionD())
                ).collect(Collectors.toList());


        return new StudentDtos.StartExamResponse(studentExam.getStudentExamId(),exam.getTitle(),questionDtos);
    }




    @Transactional
    // Submit a single answer
    public StudentDtos.AnswerDto submitAnswer(Long studentId,StudentDtos.SubmitAnswersRequest request) {

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
        LocalDateTime now =LocalDateTime.now();
        Exam exam =studentExam.getExam();


        if(exam.getExamStatus()==ExamStatus.ENDED ||now.isAfter(exam.getEndTime())){
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

        return new StudentDtos.AnswerDto(saved.getQuestion().getQuestionId(), saved.getSelected_option());
    }
  //student complete the exam
  @Transactional
    public ResultDtos.StudentExamSummary finishExam(Long studentId , ExamDtos.Request req) {
        StudentExam se = studentExamRepository.findByExamIdAndStudentId(req.examId(),studentId)
                .orElseThrow(() -> new EntryNotfoundException("StudentExam not found"));

        Exam exam =se.getExam();

     //check if exam is ended based on current time
      boolean examEnded =exam.getExamStatus()== ExamStatus.ENDED || req.isTimerEnd();
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
        long answeredQuestion =se.getStudentAnswers().size();

        if(answeredQuestion<totalQuestions){
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
     public ResultDtos.StudentResultRow getOwnResult(Long studentExamId, Long userId){
        StudentExam se = studentExamRepository.findById(studentExamId)
                .orElseThrow(() -> new EntryNotfoundException("Result  not found"));

        if(!se.getStudent().getUserId().equals(userId)){
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
            if(ans==null) {
                status = "Unanswered";
            } else if (correct) {
                status="Correct";

            }else {
                status="Wrong";
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
    ) {  //Get all published exams
        List<Exam>publishedExams=examRepository.findByExamStatus(ExamStatus.PUBLISHED);

        User student =userRepository.findById(studentId).orElseThrow(()->new EntryNotfoundException("student not found with id"+studentId));

        for(Exam exam:publishedExams) {
            boolean isEnded = exam.getExamStatus() == ExamStatus.ENDED || LocalDateTime.now().isAfter(exam.getEndTime());

            if (!isEnded) {
                boolean exists = studentExamRepository.existsByStudentAndExam(studentId, exam.getExamId());
                if (!exists) {
                    StudentExam se = new StudentExam();
                    se.setStudent(student);
                    se.setExam(exam);
                    se.setStudentExamStatus(StudentExamStatus.NEW);
                    studentExamRepository.save(se);
                }
            }
        }Pageable pageable = PageRequest.of(request.page(), request.size());
        Page<StudentExam> exams;

        if (request.search() == null || request.search().isBlank()) {
            exams = studentExamRepository.findByStudent_Id(studentId, pageable);
        } else {
            exams= studentExamRepository.findByStudentIdAndSearch(studentId, request.search(), pageable);
        }

        return exams.map(this::toStudentResponse);
    }

    private String gradeFor(double pct) {
        if (pct >= 85) return "A";
        if (pct >= 65) return "B";
        if (pct >= 55) return "C";
        if (pct >= 35) return "D";
        return "F";
    }

    private StudentDtos.StudentExamList toStudentResponse(StudentExam se) {
        Exam e = se.getExam();
        long durationMinutes = Duration.between(e.getStartTime(), e.getEndTime()).toMinutes();
        String status;
        //check if exam is already ended based on current time
        if(e.getExamStatus() == ExamStatus.ENDED || LocalDateTime.now().isAfter(e.getEndTime())){
            status="Ended";
        } else
         status = switch (se.getStudentExamStatus()) {
            case PENDING -> "pending";
            case ATTENDED -> "Attended";
            default -> "New";
        };

        return new StudentDtos.StudentExamList(
                e.getTitle(),
                e.getStartTime(),
                durationMinutes,
                status
        );
    }




}