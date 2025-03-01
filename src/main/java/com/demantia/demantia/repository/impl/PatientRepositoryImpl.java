package com.demantia.demantia.repository.impl;

import com.demantia.demantia.model.Patient;
import com.demantia.demantia.repository.PatientRepository;
import com.demantia.demantia.service.FirebaseService;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public class PatientRepositoryImpl implements PatientRepository {

    private static final String COLLECTION_NAME = "users";

    @Autowired
    private FirebaseService firebaseService;

    @Override
    public Patient getPatientById(String id) throws ExecutionException, InterruptedException {
        Map<String, Object> data = firebaseService.getData(COLLECTION_NAME, id);
        if (data == null || !"patient".equals(data.get("role"))) {
            return null;
        }
        return convertMapToPatient(id, data);
    }

    @Override
    public List<Patient> getAllPatients() throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("role", "patient")
                .get().get();

        List<Patient> patients = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            patients.add(convertMapToPatient(document.getId(), document.getData()));
        }

        return patients;
    }

    @Override
    public List<Patient> getPatientsByRiskCategory(String riskCategory)
            throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("role", "patient")
                .whereEqualTo("risk_category", riskCategory)
                .get().get();

        List<Patient> patients = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            patients.add(convertMapToPatient(document.getId(), document.getData()));
        }

        return patients;
    }

    @Override
    public List<Patient> getPatientsByCognitiveStatus(String cognitiveStatus)
            throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("role", "patient")
                .whereEqualTo("cognitive_status", cognitiveStatus)
                .get().get();

        List<Patient> patients = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            patients.add(convertMapToPatient(document.getId(), document.getData()));
        }

        return patients;
    }

    @Override
    public List<Patient> getPatientsByAgeRange(int minAge, int maxAge) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("role", "patient")
                .whereGreaterThanOrEqualTo("age", minAge)
                .whereLessThanOrEqualTo("age", maxAge)
                .get().get();

        List<Patient> patients = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            patients.add(convertMapToPatient(document.getId(), document.getData()));
        }

        return patients;
    }

    @Override
    public List<Patient> getPatientsByEducationLevel(String educationLevel)
            throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("role", "patient")
                .whereEqualTo("education_level", educationLevel)
                .get().get();

        List<Patient> patients = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            patients.add(convertMapToPatient(document.getId(), document.getData()));
        }

        return patients;
    }

    @Override
    public String createPatient(Patient patient) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        Map<String, Object> patientData = convertPatientToMap(patient);

        // Ensure role is set to "patient"
        patientData.put("role", "patient");

        String documentId = firestore.collection(COLLECTION_NAME).document().getId();
        firebaseService.saveData(COLLECTION_NAME, documentId, patientData);

        return documentId;
    }

    @Override
    public String updatePatient(Patient patient) throws ExecutionException, InterruptedException {
        Map<String, Object> patientData = convertPatientToMap(patient);
        return firebaseService.updateData(COLLECTION_NAME, patient.getId(), patientData);
    }

    @Override
    public String deletePatient(String id) throws ExecutionException, InterruptedException {
        return firebaseService.deleteData(COLLECTION_NAME, id);
    }

    private Patient convertMapToPatient(String id, Map<String, Object> data) {
        Patient patient = new Patient();
        patient.setId(id);
        patient.setName((String) data.get("name"));
        patient.setEmail((String) data.get("email"));
        patient.setRole((String) data.get("role"));

        // Patient'a özgü alanlar
        Object ageObj = data.get("age");
        if (ageObj instanceof Long) {
            patient.setAge(((Long) ageObj).intValue());
        } else if (ageObj instanceof Integer) {
            patient.setAge((Integer) ageObj);
        }

        patient.setGender((String) data.get("gender"));
        patient.setCognitive_status((String) data.get("cognitive_status"));
        patient.setEducation_level((String) data.get("education_level"));
        patient.setRisk_category((String) data.get("risk_category"));

        return patient;
    }

    private Map<String, Object> convertPatientToMap(Patient patient) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", patient.getName());
        data.put("email", patient.getEmail());
        data.put("role", patient.getRole());
        data.put("age", patient.getAge());
        data.put("gender", patient.getGender());
        data.put("cognitive_status", patient.getCognitive_status());
        data.put("education_level", patient.getEducation_level());
        data.put("risk_category", patient.getRisk_category());
        return data;
    }
}
