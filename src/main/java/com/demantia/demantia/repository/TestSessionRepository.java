package com.demantia.demantia.repository;

import com.demantia.demantia.model.TestSession;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface TestSessionRepository {
    TestSession getSessionById(String id) throws ExecutionException, InterruptedException;

    List<TestSession> getAllSessions() throws ExecutionException, InterruptedException;

    List<TestSession> getSessionsByPatientId(String patientId) throws ExecutionException, InterruptedException;

    List<TestSession> getSessionsByDoctorId(String doctorId) throws ExecutionException, InterruptedException;

    List<TestSession> getSessionsByStatus(String status) throws ExecutionException, InterruptedException;

    List<TestSession> getCompletedSessionsByPatientId(String patientId) throws ExecutionException, InterruptedException;

    TestSession getLatestSessionByPatientId(String patientId) throws ExecutionException, InterruptedException;

    String createSession(TestSession session) throws ExecutionException, InterruptedException;

    String updateSession(TestSession session) throws ExecutionException, InterruptedException;

    String completeSession(String sessionId) throws ExecutionException, InterruptedException;

    String deleteSession(String id) throws ExecutionException, InterruptedException;
}
