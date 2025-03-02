package com.demantia.demantia.controller;

import com.demantia.demantia.model.TestResult;
import com.demantia.demantia.service.TestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/test-results")
@CrossOrigin(origins = "*")
public class TestResultController {

    @Autowired
    private TestResultService testResultService;

    @GetMapping
    public ResponseEntity<List<TestResult>> getAllTestResults() {
        try {
            List<TestResult> results = testResultService.getAllTestResults();
            return new ResponseEntity<>(results, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestResult> getTestResultById(@PathVariable String id) {
        try {
            TestResult result = testResultService.getTestResultById(id);
            if (result != null) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<TestResult>> getTestResultsByPatientId(@PathVariable String patientId) {
        try {
            List<TestResult> results = testResultService.getTestResultsByPatientId(patientId);
            return new ResponseEntity<>(results, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<TestResult> getTestResultBySessionId(@PathVariable String sessionId) {
        try {
            TestResult result = testResultService.getTestResultBySessionId(sessionId);
            if (result != null) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/risk-level/{riskLevel}")
    public ResponseEntity<List<TestResult>> getTestResultsByRiskLevel(@PathVariable String riskLevel) {
        try {
            List<TestResult> results = testResultService.getTestResultsByRiskLevel(riskLevel);
            return new ResponseEntity<>(results, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<String> createTestResult(@RequestBody TestResult testResult) {
        try {
            String id = testResultService.createTestResult(testResult);
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateTestResult(@RequestBody Map<String, String> request) {
        try {
            String sessionId = request.get("sessionId");
            String patientId = request.get("patientId");

            String resultId = testResultService.generateTestResult(sessionId, patientId);
            return new ResponseEntity<>(resultId, HttpStatus.CREATED);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTestResult(@PathVariable String id, @RequestBody TestResult testResult) {
        try {
            testResult.setId(id);
            String result = testResultService.updateTestResult(testResult);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTestResult(@PathVariable String id) {
        try {
            String result = testResultService.deleteTestResult(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
