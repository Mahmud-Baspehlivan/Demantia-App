package com.demantia.demantia.repository;

import com.demantia.demantia.model.TestResult;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface TestResultRepository {
    TestResult getTestResultById(String id) throws ExecutionException, InterruptedException;

    List<TestResult> getAllTestResults() throws ExecutionException, InterruptedException;

    List<TestResult> getTestResultsByPatientId(String patientId) throws ExecutionException, InterruptedException;

    TestResult getTestResultBySessionId(String testSessionId) throws ExecutionException, InterruptedException;

    List<TestResult> getTestResultsByRiskLevel(String riskLevel) throws ExecutionException, InterruptedException;

    String createTestResult(TestResult testResult) throws ExecutionException, InterruptedException;

    String updateTestResult(TestResult testResult) throws ExecutionException, InterruptedException;

    String deleteTestResult(String id) throws ExecutionException, InterruptedException;
}
