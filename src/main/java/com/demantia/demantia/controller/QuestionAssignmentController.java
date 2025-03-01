package com.demantia.demantia.controller;

import com.demantia.demantia.model.QuestionAssignment;
import com.demantia.demantia.model.Patient;
import com.demantia.demantia.service.QuestionAssignmentService;
import com.demantia.demantia.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/question-assignments")
@CrossOrigin(origins = "*")
public class QuestionAssignmentController {

    @Autowired
    private QuestionAssignmentService assignmentService;

    @Autowired
    private PatientService patientService;

    @GetMapping("/{id}")
    public ResponseEntity<QuestionAssignment> getAssignmentById(@PathVariable String id) {
        try {
            QuestionAssignment assignment = assignmentService.getAssignmentById(id);
            if (assignment != null) {
                return new ResponseEntity<>(assignment, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<QuestionAssignment>> getAllAssignments() {
        try {
            List<QuestionAssignment> assignments = assignmentService.getAllAssignments();
            return new ResponseEntity<>(assignments, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<QuestionAssignment>> getAssignmentsByQuestionId(@PathVariable String questionId) {
        try {
            List<QuestionAssignment> assignments = assignmentService.getAssignmentsByQuestionId(questionId);
            return new ResponseEntity<>(assignments, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/difficulty-level/{difficultyLevel}")
    public ResponseEntity<List<QuestionAssignment>> getAssignmentsByDifficultyLevel(
            @PathVariable String difficultyLevel) {
        try {
            List<QuestionAssignment> assignments = assignmentService.getAssignmentsByDifficultyLevel(difficultyLevel);
            return new ResponseEntity<>(assignments, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/education-level/{educationLevel}")
    public ResponseEntity<List<QuestionAssignment>> getAssignmentsByEducationLevel(
            @PathVariable String educationLevel) {
        try {
            List<QuestionAssignment> assignments = assignmentService.getAssignmentsByEducationLevel(educationLevel);
            return new ResponseEntity<>(assignments, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/risk-category/{riskCategory}")
    public ResponseEntity<List<QuestionAssignment>> getAssignmentsByRiskCategory(@PathVariable String riskCategory) {
        try {
            List<QuestionAssignment> assignments = assignmentService.getAssignmentsByRiskCategory(riskCategory);
            return new ResponseEntity<>(assignments, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/age/{age}")
    public ResponseEntity<List<QuestionAssignment>> getAssignmentsForAgeGroup(@PathVariable int age) {
        try {
            List<QuestionAssignment> assignments = assignmentService.getAssignmentsForAgeGroup(age);
            return new ResponseEntity<>(assignments, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<QuestionAssignment>> getAssignmentsForPatient(@PathVariable String patientId) {
        try {
            Patient patient = patientService.getPatientById(patientId);
            if (patient == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            List<QuestionAssignment> assignments = assignmentService.getAssignmentsForPatient(patient);
            return new ResponseEntity<>(assignments, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/session/{testSessionId}")
    public ResponseEntity<List<QuestionAssignment>> getAssignmentsBySessionId(@PathVariable String testSessionId) {
        try {
            List<QuestionAssignment> assignments = assignmentService.getAssignmentsBySessionId(testSessionId);
            return new ResponseEntity<>(assignments, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<String> createAssignment(@RequestBody QuestionAssignment assignment) {
        try {
            String id = assignmentService.createAssignment(assignment);
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAssignment(@PathVariable String id,
            @RequestBody QuestionAssignment assignment) {
        try {
            assignment.setId(id);
            String result = assignmentService.updateAssignment(assignment);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAssignment(@PathVariable String id) {
        try {
            String result = assignmentService.deleteAssignment(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
