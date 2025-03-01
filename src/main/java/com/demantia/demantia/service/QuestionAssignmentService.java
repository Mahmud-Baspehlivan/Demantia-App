package com.demantia.demantia.service;

import com.demantia.demantia.model.QuestionAssignment;
import com.demantia.demantia.model.Patient;
import com.demantia.demantia.repository.QuestionAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.ExecutionException;

@Service
public class QuestionAssignmentService {

    @Autowired
    private QuestionAssignmentRepository questionAssignmentRepository;

    public QuestionAssignment getAssignmentById(String id) throws ExecutionException, InterruptedException {
        return questionAssignmentRepository.getAssignmentById(id);
    }

    public List<QuestionAssignment> getAllAssignments() throws ExecutionException, InterruptedException {
        return questionAssignmentRepository.getAllAssignments();
    }

    // Metot adını ve parametre adını güncelle
    public List<QuestionAssignment> getAssignmentsBySessionId(String testSessionId)
            throws ExecutionException, InterruptedException {
        return questionAssignmentRepository.getAssignmentsBySessionId(testSessionId);
    }

    public List<QuestionAssignment> getAssignmentsByQuestionId(String questionId)
            throws ExecutionException, InterruptedException {
        return questionAssignmentRepository.getAssignmentsByQuestionId(questionId);
    }

    public List<QuestionAssignment> getAssignmentsByDifficultyLevel(String difficultyLevel)
            throws ExecutionException, InterruptedException {
        return questionAssignmentRepository.getAssignmentsByDifficultyLevel(difficultyLevel);
    }

    public List<QuestionAssignment> getAssignmentsByEducationLevel(String educationLevel)
            throws ExecutionException, InterruptedException {
        return questionAssignmentRepository.getAssignmentsByEducationLevel(educationLevel);
    }

    public List<QuestionAssignment> getAssignmentsByRiskCategory(String riskCategory)
            throws ExecutionException, InterruptedException {
        return questionAssignmentRepository.getAssignmentsByRiskCategory(riskCategory);
    }

    public List<QuestionAssignment> getAssignmentsForAgeGroup(int age) throws ExecutionException, InterruptedException {
        return questionAssignmentRepository.getAssignmentsForAgeGroup(age);
    }

    public String createAssignment(QuestionAssignment assignment) throws ExecutionException, InterruptedException {
        return questionAssignmentRepository.createAssignment(assignment);
    }

    public String updateAssignment(QuestionAssignment assignment) throws ExecutionException, InterruptedException {
        return questionAssignmentRepository.updateAssignment(assignment);
    }

    public String deleteAssignment(String id) throws ExecutionException, InterruptedException {
        return questionAssignmentRepository.deleteAssignment(id);
    }

    // İş mantığı için yardımcı metodlar

    public List<QuestionAssignment> getAssignmentsForPatient(Patient patient)
            throws ExecutionException, InterruptedException {
        List<QuestionAssignment> allAssignments = getAllAssignments();

        return allAssignments.stream()
                .filter(a -> matchesPatientProfile(a, patient))
                .collect(Collectors.toList());
    }

    private boolean matchesPatientProfile(QuestionAssignment assignment, Patient patient) {
        int patientAge = patient.getAge() != null ? patient.getAge() : 0;

        boolean ageMatch = (assignment.getMin_age() == null || patientAge >= assignment.getMin_age()) &&
                (assignment.getMax_age() == null || patientAge <= assignment.getMax_age());

        boolean eduMatch = assignment.getEducation_level() == null ||
                assignment.getEducation_level().equals(patient.getEducation_level());

        boolean riskMatch = assignment.getRisk_category() == null ||
                assignment.getRisk_category().equals(patient.getRisk_category());

        return ageMatch && eduMatch && riskMatch;
    }
}
