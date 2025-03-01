package com.demantia.demantia.repository;

import com.demantia.demantia.model.TestResponse;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface TestResponseRepository {
    TestResponse getResponseById(String id) throws ExecutionException, InterruptedException;

    List<TestResponse> getAllResponses() throws ExecutionException, InterruptedException;

    List<TestResponse> getResponsesBySessionId(String testSessionId) throws ExecutionException, InterruptedException;

    List<TestResponse> getResponsesByPatientId(String patientId) throws ExecutionException, InterruptedException;

    List<TestResponse> getResponsesByQuestionId(String questionId) throws ExecutionException, InterruptedException;

    TestResponse getResponseBySessionAndQuestionId(String sessionId, String questionId)
            throws ExecutionException, InterruptedException;

    String createResponse(TestResponse testResponse) throws ExecutionException, InterruptedException;

    String updateResponse(TestResponse testResponse) throws ExecutionException, InterruptedException;

    String deleteResponse(String id) throws ExecutionException, InterruptedException;
}
