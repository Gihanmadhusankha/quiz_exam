package com.quiz.quiz_exam.service.Impl;

import com.quiz.quiz_exam.dto.requestDto.ExamRequestDto;
import com.quiz.quiz_exam.dto.responseDto.ExamResponseDto;
import com.quiz.quiz_exam.dto.responseDto.paginate.ExamPaginateResponseDto;
import com.quiz.quiz_exam.entity.Exam;
import com.quiz.quiz_exam.exceptions.EntryNotFoundException;
import com.quiz.quiz_exam.repository.ExamRepository;
import com.quiz.quiz_exam.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;
    @Override
    public ExamResponseDto addExam(ExamRequestDto examRequestDto) {
        Exam savedExam = examRepository.save(toExam(examRequestDto));
        return toExamResponseDto(savedExam);
    }

    @Override
    public ExamResponseDto updateExam(String examId, ExamRequestDto examRequestDto) {
        Exam selectedExam = examRepository.findExam(examId)
                .orElseThrow(() -> new EntryNotFoundException("Exam not found"));

        selectedExam.setTitle(examRequestDto.getTitle());
        selectedExam.setExamDate(examRequestDto.getExamDate());
        selectedExam.setStartTime(examRequestDto.getStartTime());
        selectedExam.setEndTime(examRequestDto.getEndTime());
        selectedExam.setStatus(examRequestDto.getStatus());

        examRepository.save(selectedExam);

        return toExamResponseDto(selectedExam);
    }



    @Override
    public ExamPaginateResponseDto findAll(int page, int size, String searchText) {
        var resultPage = examRepository.searchAllExams(searchText, PageRequest.of(page, size));
        return ExamPaginateResponseDto.builder()
                .dataList(resultPage.getContent().stream()
                        .map(this::toExamResponseDto)
                        .collect(Collectors.toList()))

                .build();
    }
    @Override
    public void deleteExam(String examId) {
        examRepository.findExam(examId)
                .orElseThrow(() -> new EntryNotFoundException("Exam Not found"));
        examRepository.deleteById(Integer.valueOf(examId));
    }

    private Exam toExam(ExamRequestDto dto) {
        return Exam.builder()
                .examId(dto.getExamId())
                .title(dto.getTitle())
                .examDate(dto.getExamDate())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .status(dto.getStatus())
                .build();
    }

    private ExamResponseDto toExamResponseDto(Exam exam) {
        return ExamResponseDto.builder()
                .examId(exam.getExamId())
                .title(exam.getTitle())
                .examDate(exam.getExamDate())
                .startTime(exam.getStartTime())
                .endTime(exam.getEndTime())
                .status(exam.getStatus())
                .build();
    }
}
