package com.demantia.demantia.controller;

import com.demantia.demantia.model.TestSession;
import com.demantia.demantia.service.TestSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/test-sessions")
@CrossOrigin(origins = "*")
public class TestSessionController {

    @Autowired
    private TestSessionService sessionService;

    @GetMapping
    public ResponseEntity<List<TestSession>> getAllSessions() {
        try {
            List<TestSession> sessions = sessionService.getAllSessions();
            return new ResponseEntity<>(sessions, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestSession> getSessionById(@PathVariable String id) {
        try {
            TestSession session = sessionService.getSessionById(id);
            if (session != null) {
                return new ResponseEntity<>(session, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<TestSession>> getSessionsByPatientId(@PathVariable String patientId) {
        try {
            List<TestSession> sessions = sessionService.getSessionsByPatientId(patientId);
            return new ResponseEntity<>(sessions, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/patient/{patientId}/active")
    public ResponseEntity<TestSession> getActiveSessionForPatient(@PathVariable String patientId) {
        try {
            TestSession session = sessionService.getActiveSessionForPatient(patientId);
            if (session != null) {
                return new ResponseEntity<>(session, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/patient/{patientId}/completed")
    public ResponseEntity<List<TestSession>> getCompletedSessionsByPatientId(@PathVariable String patientId) {
        try {
            List<TestSession> sessions = sessionService.getCompletedSessionsByPatientId(patientId);
            return new ResponseEntity<>(sessions, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/patient/{patientId}/latest")
    public ResponseEntity<TestSession> getLatestSessionByPatientId(@PathVariable String patientId) {
        try {
            TestSession session = sessionService.getLatestSessionByPatientId(patientId);
            if (session != null) {
                return new ResponseEntity<>(session, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<TestSession>> getSessionsByDoctorId(@PathVariable String doctorId) {
        try {
            List<TestSession> sessions = sessionService.getSessionsByDoctorId(doctorId);
            return new ResponseEntity<>(sessions, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TestSession>> getSessionsByStatus(@PathVariable String status) {
        try {
            List<TestSession> sessions = sessionService.getSessionsByStatus(status);
            return new ResponseEntity<>(sessions, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<String> createSession(@RequestBody TestSession session) {
        try {
            String id = sessionService.createSession(session);
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/with-questions")
    public ResponseEntity<String> createSessionWithQuestions(@RequestBody Map<String, Object> request) {
        try {
            String patientId = (String) request.get("patientId");
            String doctorId = (String) request.get("doctorId");
            @SuppressWarnings("unchecked")
            List<String> questionIds = (List<String>) request.get("questionIds");

            String sessionId = sessionService.createSessionWithQuestions(patientId, doctorId, questionIds);
            return new ResponseEntity<>(sessionId, HttpStatus.CREATED);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<String> completeSession(@PathVariable String id) {
        try {
            String result = sessionService.completeSession(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/duration")
    public ResponseEntity<Long> getSessionDuration(@PathVariable String id) {
        try {
            TestSession session = sessionService.getSessionById(id);
            if (session != null) {
                long duration = sessionService.calculateSessionDurationSeconds(session);
                if (duration >= 0) {
                    return new ResponseEntity<>(duration, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSession(@PathVariable String id, @RequestBody TestSession session) {
        try {
            session.setId(id);
            String result = sessionService.updateSession(session);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSession(@PathVariable String id) {
        try {
            String result = sessionService.deleteSession(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
