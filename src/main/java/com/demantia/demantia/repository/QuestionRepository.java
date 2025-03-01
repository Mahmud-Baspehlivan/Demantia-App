package com.demantia.demantia.repository;

import com.demantia.demantia.model.Question;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface QuestionRepository {
    Question getQuestionById(String id) throws ExecutionException, InterruptedException;

    List<Question> getAllQuestions() throws ExecutionException, InterruptedException;

    List<Question> getQuestionsByCategory(String category) throws ExecutionException, InterruptedException;

    List<Question> getQuestionsByRelatedGroup(String relatedGroup) throws ExecutionException, InterruptedException;

    List<Question> getQuestionsByDifficultyLevel(String difficultyLevel)
            throws ExecutionException, InterruptedException;

    List<Question> getQuestionsByIds(List<String> ids) throws ExecutionException, InterruptedException;

    String createQuestion(Question question) throws ExecutionException, InterruptedException;

    String updateQuestion(Question question) throws ExecutionException, InterruptedException;

    String deleteQuestion(String id) throws ExecutionException, InterruptedException;
}
