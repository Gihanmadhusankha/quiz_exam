package com.quiz.quiz_exam.service.Impl;

import com.quiz.quiz_exam.dto.QuestionDtos;
import com.quiz.quiz_exam.entity.Exam;
import com.quiz.quiz_exam.entity.Question;
import com.quiz.quiz_exam.repository.ExamRepository;
import com.quiz.quiz_exam.repository.QuestionRepository;
import com.quiz.quiz_exam.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final ExamRepository examRepository;

    @Override
    public QuestionDtos.QuestionResponse addQuestion(QuestionDtos.CreateQuestionRequest req) {
        Exam exam = examRepository.findById(req.examId())
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        Question q = new Question();
        q.setExam(exam);
        q.setQuestionText(req.questionText());
        q.setOptionA(req.optionA());
        q.setOptionB(req.optionB());
        q.setOptionC(req.optionC());
        q.setOptionD(req.optionD());
        q.setCorrectOption(req.correctAnswer());

        Question saved = questionRepository.save(q);

        return new QuestionDtos.QuestionResponse(
                saved.getQuestionId(),
                exam.getExamId(),
                saved.getQuestionText(),
                saved.getOptionA(),
                saved.getOptionB(),
                saved.getOptionC(),
                saved.getOptionD(),
                saved.getCorrectOption()
        );
    }

    @Override
    public List<QuestionDtos.QuestionResponse> listByExam(Long examId) {
        return questionRepository.findByExam_ExamId(examId)
                .stream()
                .map(q -> new QuestionDtos.QuestionResponse(
                        q.getQuestionId(),
                        q.getExam().getExamId(),
                        q.getQuestionText(),
                        q.getOptionA(),
                        q.getOptionB(),
                        q.getOptionC(),
                        q.getOptionD(),
                        q.getCorrectOption()
                ))
                .collect(Collectors.toList());
    }
}
