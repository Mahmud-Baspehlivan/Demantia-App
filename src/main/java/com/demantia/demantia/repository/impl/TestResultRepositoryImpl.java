package com.demantia.demantia.repository.impl;

import com.demantia.demantia.model.TestResult;
import com.demantia.demantia.repository.TestResultRepository;
import com.demantia.demantia.service.FirebaseService;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public class TestResultRepositoryImpl implements TestResultRepository {

    private static final String COLLECTION_NAME = "test_results";

    @Autowired
    private FirebaseService firebaseService;

    @Override
    public TestResult getTestResultById(String id) throws ExecutionException, InterruptedException {
        Map<String, Object> data = firebaseService.getData(COLLECTION_NAME, id);
        if (data == null) {
            return null;
        }
        return convertMapToTestResult(id, data);
    }

    @Override
    public List<TestResult> getAllTestResults() throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME).get().get();

        List<TestResult> testResults = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            testResults.add(convertMapToTestResult(document.getId(), document.getData()));
        }

        return testResults;
    }

    @Override
    public List<TestResult> getTestResultsByPatientId(String patientId)
            throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("patientId", patientId)
                .get().get();

        List<TestResult> testResults = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            testResults.add(convertMapToTestResult(document.getId(), document.getData()));
        }

        return testResults;
    }

    @Override
    public TestResult getTestResultBySessionId(String sessionId) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("testSessionId", sessionId)
                .get().get();

        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        if (documents.isEmpty()) {
            return null;
        }

        QueryDocumentSnapshot document = documents.get(0);
        return convertMapToTestResult(document.getId(), document.getData());
    }

    @Override
    public String createTestResult(TestResult testResult) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        String timestamp = LocalDateTime.now().toString();
        testResult.setCreatedAt(timestamp);
        testResult.setUpdatedAt(timestamp);

        Map<String, Object> resultData = convertTestResultToMap(testResult);

        String documentId = firestore.collection(COLLECTION_NAME).document().getId();
        firebaseService.saveData(COLLECTION_NAME, documentId, resultData);

        return documentId;
    }

    @Override
    public String updateTestResult(TestResult testResult) throws ExecutionException, InterruptedException {
        testResult.setUpdatedAt(LocalDateTime.now().toString());
        Map<String, Object> resultData = convertTestResultToMap(testResult);
        return firebaseService.updateData(COLLECTION_NAME, testResult.getId(), resultData);
    }

    @Override
    public String deleteTestResult(String id) throws ExecutionException, InterruptedException {
        return firebaseService.deleteData(COLLECTION_NAME, id);
    }

    @Override
    public List<TestResult> getTestResultsByRiskLevel(String riskLevel)
            throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("risk_level", riskLevel)
                .get().get();

        List<TestResult> testResults = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            testResults.add(convertMapToTestResult(document.getId(), document.getData()));
        }

        return testResults;
    }

    private TestResult convertMapToTestResult(String id, Map<String, Object> data) {
        TestResult testResult = new TestResult();
        testResult.setId(id);
        testResult.setTest_session_id((String) data.get("test_session_id"));
        testResult.setPatient_id((String) data.get("patient_id"));
        testResult.setRecommendation((String) data.get("recommendation"));

        // Overall score için güvenli dönüşüm
        Object overallScoreObj = data.get("overall_score");
        if (overallScoreObj instanceof Long) {
            testResult.setOverall_score(((Long) overallScoreObj).intValue());
        } else if (overallScoreObj instanceof Integer) {
            testResult.setOverall_score((Integer) overallScoreObj);
        }

        testResult.setRisk_level((String) data.get("risk_level"));
        testResult.setDoctor_comments((String) data.get("doctor_comments"));
        testResult.setCreatedAt((String) data.get("createdAt"));
        testResult.setUpdatedAt((String) data.get("updatedAt"));
        return testResult;
    }

    private Map<String, Object> convertTestResultToMap(TestResult testResult) {
        Map<String, Object> data = new HashMap<>();
        data.put("test_session_id", testResult.getTest_session_id());
        data.put("patient_id", testResult.getPatient_id());
        data.put("recommendation", testResult.getRecommendation());
        data.put("overall_score", testResult.getOverall_score());
        data.put("risk_level", testResult.getRisk_level());
        data.put("doctor_comments", testResult.getDoctor_comments());
        data.put("createdAt", testResult.getCreatedAt());
        data.put("updatedAt", testResult.getUpdatedAt());
        return data;
    }
}
