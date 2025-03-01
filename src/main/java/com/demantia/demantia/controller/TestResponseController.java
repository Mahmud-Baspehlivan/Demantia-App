package com.demantia.demantia.controller;

import com.demantia.demantia.model.TestResponse;
import com.demantia.demantia.service.TestResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/test-responses")
@CrossOrigin(origins = "*")
public class TestResponseController {

    @Autowired
    private TestResponseService testResponseService;

    @GetMapping("/{id}")
    public ResponseEntity<TestResponse> getResponseById(@PathVariable String id) {
        try {
            TestResponse response = testResponseService.getResponseById(id);
            if (response != null) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<TestResponse>> getResponsesBySessionId(@PathVariable String sessionId) {
        try {
            List<TestResponse> responses = testResponseService.getResponsesBySessionId(sessionId);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<TestResponse>> getResponsesByPatientId(@PathVariable String patientId) {
        try {
            List<TestResponse> responses = testResponseService.getResponsesByPatientId(patientId);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<TestResponse>> getResponsesByQuestionId(@PathVariable String questionId) {
        try {
            List<TestResponse> responses = testResponseService.getResponsesByQuestionId(questionId);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/session/{sessionId}/question/{questionId}")
    public ResponseEntity<TestResponse> getResponseBySessionAndQuestionId(
            @PathVariable String sessionId, @PathVariable String questionId) {
        try {
            TestResponse response = testResponseService.getResponseBySessionAndQuestionId(sessionId, questionId);
            if (response != null) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/session/{sessionId}/scores")
    public ResponseEntity<Map<String, Double>> getCategoryScores(@PathVariable String sessionId) {
        try {
            Map<String, Double> scores = testResponseService.calculateCategoryScores(sessionId);
            return new ResponseEntity<>(scores, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<String> createResponse(@RequestBody TestResponse response) {
        try {
            String id = testResponseService.createResponse(response);
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/with-scoring")
    public ResponseEntity<String> createResponseWithScoring(
            @RequestBody Map<String, Object> requestBody) {
        try {
            String questionId = (String) requestBody.get("question_id");
            String testSessionId = (String) requestBody.get("test_session_id");
            String patientId = (String) requestBody.get("patient_id");
            String answer = (String) requestBody.get("answer");

            TestResponse response = new TestResponse();
            response.setQuestion_id(questionId);
            response.setTest_session_id(testSessionId);
            response.setPatient_id(patientId);

            String id = testResponseService.createResponseWithScoring(response, answer);
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateResponse(@PathVariable String id, @RequestBody TestResponse response) {
        try {
            response.setId(id);
            String result = testResponseService.updateResponse(response);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteResponse(@PathVariable String id) {
        try {
            String result = testResponseService.deleteResponse(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
