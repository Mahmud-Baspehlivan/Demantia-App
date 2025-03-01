package com.demantia.demantia.service;

import com.demantia.demantia.model.Patient;
import com.demantia.demantia.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public Patient getPatientById(String id) throws ExecutionException, InterruptedException {
        return patientRepository.getPatientById(id);
    }

    public List<Patient> getAllPatients() throws ExecutionException, InterruptedException {
        return patientRepository.getAllPatients();
    }

    public List<Patient> getPatientsByRiskCategory(String riskCategory)
            throws ExecutionException, InterruptedException {
        return patientRepository.getPatientsByRiskCategory(riskCategory);
    }

    public List<Patient> getPatientsByCognitiveStatus(String cognitiveStatus)
            throws ExecutionException, InterruptedException {
        return patientRepository.getPatientsByCognitiveStatus(cognitiveStatus);
    }

    public List<Patient> getPatientsByAgeRange(int minAge, int maxAge) throws ExecutionException, InterruptedException {
        return patientRepository.getPatientsByAgeRange(minAge, maxAge);
    }

    public List<Patient> getPatientsByEducationLevel(String educationLevel)
            throws ExecutionException, InterruptedException {
        return patientRepository.getPatientsByEducationLevel(educationLevel);
    }

    public String createPatient(Patient patient) throws ExecutionException, InterruptedException {
        // Rol kontrolü
        if (patient.getRole() == null || !patient.getRole().equals("patient")) {
            patient.setRole("patient");
        }

        return patientRepository.createPatient(patient);
    }

    public String updatePatient(Patient patient) throws ExecutionException, InterruptedException {
        return patientRepository.updatePatient(patient);
    }

    public String deletePatient(String id) throws ExecutionException, InterruptedException {
        return patientRepository.deletePatient(id);
    }

    // İş mantığı için yardımcı metodlar

    public String assessRiskCategory(Patient patient) {
        int age = patient.getAge() != null ? patient.getAge() : 0;
        String cognitiveStatus = patient.getCognitive_status();

        // Basit bir risk kategorizasyonu
        if (age >= 70 || "Orta Demans".equals(cognitiveStatus) || "İleri Demans".equals(cognitiveStatus)) {
            return "high";
        } else if (age >= 60 || "Erken Evre Demans".equals(cognitiveStatus) || "MCI".equals(cognitiveStatus)) {
            return "medium";
        } else {
            return "low";
        }
    }

    public void updateRiskCategory(String patientId) throws ExecutionException, InterruptedException {
        Patient patient = getPatientById(patientId);
        if (patient != null) {
            String riskCategory = assessRiskCategory(patient);
            patient.setRisk_category(riskCategory);
            updatePatient(patient);
        }
    }
}
