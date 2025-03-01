package com.demantia.demantia.repository;

import com.demantia.demantia.model.Patient;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface PatientRepository {
    Patient getPatientById(String id) throws ExecutionException, InterruptedException;

    List<Patient> getAllPatients() throws ExecutionException, InterruptedException;

    List<Patient> getPatientsByRiskCategory(String riskCategory) throws ExecutionException, InterruptedException;

    List<Patient> getPatientsByCognitiveStatus(String cognitiveStatus) throws ExecutionException, InterruptedException;

    List<Patient> getPatientsByAgeRange(int minAge, int maxAge) throws ExecutionException, InterruptedException;

    List<Patient> getPatientsByEducationLevel(String educationLevel) throws ExecutionException, InterruptedException;

    String createPatient(Patient patient) throws ExecutionException, InterruptedException;

    String updatePatient(Patient patient) throws ExecutionException, InterruptedException;

    String deletePatient(String id) throws ExecutionException, InterruptedException;
}
