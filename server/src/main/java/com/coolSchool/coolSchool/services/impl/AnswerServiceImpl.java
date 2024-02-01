package com.coolSchool.coolSchool.services.impl;

import com.coolSchool.coolSchool.exceptions.answer.AnswerNotFoundException;
import com.coolSchool.coolSchool.exceptions.answer.ValidationAnswerException;
import com.coolSchool.coolSchool.exceptions.common.NoSuchElementException;
import com.coolSchool.coolSchool.models.dto.common.AnswerDTO;
import com.coolSchool.coolSchool.models.entity.Answer;
import com.coolSchool.coolSchool.repositories.AnswerRepository;
import com.coolSchool.coolSchool.repositories.QuestionRepository;
import com.coolSchool.coolSchool.services.AnswerService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;

import java.util.List;
import java.util.Optional;

@Service
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final ModelMapper modelMapper;
    private final QuestionRepository questionRepository;
    private final Validator validator;
    private final MessageSource messageSource;

    public AnswerServiceImpl(AnswerRepository answerRepository, ModelMapper modelMapper, QuestionRepository questionRepository, Validator validator, MessageSource messageSource) {
        this.answerRepository = answerRepository;
        this.modelMapper = modelMapper;
        this.questionRepository = questionRepository;
        this.validator = validator;
        this.messageSource = messageSource;
    }

    @Override
    public List<AnswerDTO> getAllAnswers() {
        List<Answer> answers = answerRepository.findByDeletedFalse();
        return answers.stream().map(answer -> modelMapper.map(answer, AnswerDTO.class)).toList();
    }

    @Override
    public AnswerDTO getAnswerById(Long id) {
        Optional<Answer> answer = answerRepository.findByIdAndDeletedFalse(id);
        if (answer.isPresent()) {
            return modelMapper.map(answer.get(), AnswerDTO.class);
        }
        throw new AnswerNotFoundException(messageSource);
    }

    @Override
    public AnswerDTO createAnswer(AnswerDTO answerDTO) {
        try {
            answerDTO.setId(null);
            questionRepository.findByIdAndDeletedFalse(answerDTO.getQuestionId()).orElseThrow(()-> new NoSuchElementException(messageSource));
            Answer answerEntity = answerRepository.save(modelMapper.map(answerDTO, Answer.class));
            return modelMapper.map(answerEntity, AnswerDTO.class);
        } catch (ConstraintViolationException exception) {
            throw new ValidationAnswerException(exception.getConstraintViolations());
        }
    }

    @Override
    public AnswerDTO updateAnswer(Long id, AnswerDTO answerDTO) {
        Optional<Answer> existingAnswerOptional = answerRepository.findByIdAndDeletedFalse(id);

        if (existingAnswerOptional.isEmpty()) {
            throw new AnswerNotFoundException(messageSource);
        }
        questionRepository.findByIdAndDeletedFalse(answerDTO.getQuestionId()).orElseThrow(()-> new NoSuchElementException(messageSource));

        Answer existingAnswer = existingAnswerOptional.get();
        modelMapper.map(answerDTO, existingAnswer);

        try {
            existingAnswer.setId(id);
            Answer updatedAnswer = answerRepository.save(existingAnswer);
            return modelMapper.map(updatedAnswer, AnswerDTO.class);
        } catch (TransactionException exception) {
            if (exception.getRootCause() instanceof ConstraintViolationException validationException) {
                throw new ValidationAnswerException(validationException.getConstraintViolations());
            }
            throw exception;
        }
    }

    @Override
    public void deleteAnswer(Long id) {
        Optional<Answer> answer = answerRepository.findByIdAndDeletedFalse(id);
        if (answer.isPresent()) {
            answer.get().setDeleted(true);
            answerRepository.save(answer.get());
        } else {
            throw new AnswerNotFoundException(messageSource);
        }
    }
    public List<AnswerDTO> getCorrectAnswersByQuestionId(Long questionId) {
        List<Answer> correctAnswers = answerRepository.findCorrectAnswersByQuestionId(questionId);
        return correctAnswers.stream().map(answer -> modelMapper.map(answer, AnswerDTO.class)).toList();
    }
    public List<AnswerDTO> getAnswersByQuestionId(Long questionId) {
        List<Answer> answers = answerRepository.findAnswersByQuestionId(questionId);
        return answers.stream().map(answer -> modelMapper.map(answer, AnswerDTO.class)).toList();
    }
}
