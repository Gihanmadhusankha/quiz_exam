package com.quiz.quiz_exam.service.Impl;

import com.quiz.quiz_exam.dto.requestDto.QuestionRequestDto;
import com.quiz.quiz_exam.dto.responseDto.QuestionResponseDto;
import com.quiz.quiz_exam.entity.Question;
import com.quiz.quiz_exam.exceptions.EntryNotFoundException;
import com.quiz.quiz_exam.repository.QuestionRepository;
import com.quiz.quiz_exam.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository repository;

    @Override
    public QuestionResponseDto addQuestion(QuestionRequestDto questionRequestDto) {

        Question question = repository.save(toQuestion(questionRequestDto));
        return toQuestionResponseDto(question);

    }

    @Override
    public QuestionResponseDto findQuestion(int questionId) {
        Question selectedQuestion = repository.findById(questionId)
                .orElseThrow(() -> new EntryNotFoundException("Question Not found"));
        return toQuestionResponseDto(selectedQuestion);

    }

    @Override
    public void deleteQuestion(int questionId) {
        repository.findById(questionId)
                .orElseThrow(() -> new EntryNotFoundException("Question Not found"));
        repository.deleteById(questionId);

    }

    @Override
    public void updateQuestion(int questionId, QuestionRequestDto questionRequestDto) {
        Question selectedQuestion = repository.findById(questionId)
                .orElseThrow(() -> new EntryNotFoundException("Question Not found"));
        selectedQuestion.setQuestionText(questionRequestDto.getQuestionText());
        selectedQuestion.setOption_a(questionRequestDto.getOption_a());
        selectedQuestion.setOption_b(questionRequestDto.getOption_b());
        selectedQuestion.setOption_c(questionRequestDto.getOption_c());
        selectedQuestion.setOption_d(questionRequestDto.getOption_d());
        repository.save(selectedQuestion);

    }

    private Question toQuestion(QuestionRequestDto questionRequestDto) {
        return Question.builder()
                .questionText(questionRequestDto.getQuestionText())
                .option_a(questionRequestDto.getOption_a())
                .option_b(questionRequestDto.getOption_b())
                .option_c(questionRequestDto.getOption_c())
                .option_d(questionRequestDto.getOption_d())
                .correct_option(questionRequestDto.getCorrect_option())
                .build();
    }

    private QuestionResponseDto toQuestionResponseDto(Question question) {
        return QuestionResponseDto.builder()
                .questionText(question.getQuestionText())
                .option_a(question.getOption_a())
                .option_b(question.getOption_b())
                .option_c(question.getOption_c())
                .option_d(question.getOption_d())
                .build();
    }
}