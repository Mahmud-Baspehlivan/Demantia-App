package com.demantia.demantia.repository;

import com.demantia.demantia.model.QuestionAssignment;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface QuestionAssignmentRepository {
    QuestionAssignment getAssignmentById(String id) throws ExecutionException, InterruptedException;

    List<QuestionAssignment> getAllAssignments() throws ExecutionException, InterruptedException;

    List<QuestionAssignment> getAssignmentsBySessionId(String testSessionId)
            throws ExecutionException, InterruptedException;

    List<QuestionAssignment> getAssignmentsByQuestionId(String questionId)
            throws ExecutionException, InterruptedException;

    List<QuestionAssignment> getAssignmentsByDifficultyLevel(String difficultyLevel)
            throws ExecutionException, InterruptedException;

    List<QuestionAssignment> getAssignmentsByEducationLevel(String educationLevel)
            throws ExecutionException, InterruptedException;

    List<QuestionAssignment> getAssignmentsByRiskCategory(String riskCategory)
            throws ExecutionException, InterruptedException;

    List<QuestionAssignment> getAssignmentsForAgeGroup(int age) throws ExecutionException, InterruptedException;

    String createAssignment(QuestionAssignment assignment) throws ExecutionException, InterruptedException;

    String updateAssignment(QuestionAssignment assignment) throws ExecutionException, InterruptedException;

    String deleteAssignment(String id) throws ExecutionException, InterruptedException;
}
