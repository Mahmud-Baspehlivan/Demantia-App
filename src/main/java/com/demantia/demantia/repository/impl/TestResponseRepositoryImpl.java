package com.demantia.demantia.repository.impl;

import com.demantia.demantia.model.TestResponse;
import com.demantia.demantia.repository.TestResponseRepository;
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
public class TestResponseRepositoryImpl implements TestResponseRepository {

    private static final String COLLECTION_NAME = "test_responses";

    @Autowired
    private FirebaseService firebaseService;

    @Override
    public TestResponse getResponseById(String id) throws ExecutionException, InterruptedException {
        Map<String, Object> data = firebaseService.getData(COLLECTION_NAME, id);
        if (data == null) {
            return null;
        }
        return convertMapToTestResponse(id, data);
    }

    @Override
    public List<TestResponse> getResponsesBySessionId(String sessionId)
            throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                // Alan adını onaylayın - bu "test_session_id" olmalı
                .whereEqualTo("test_session_id", sessionId)
                .get().get();

        List<TestResponse> responses = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            responses.add(convertMapToTestResponse(document.getId(), document.getData()));
        }

        return responses;
    }

    @Override
    public List<TestResponse> getResponsesByPatientId(String patientId)
            throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("patient_id", patientId)
                .get().get();

        List<TestResponse> responses = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            responses.add(convertMapToTestResponse(document.getId(), document.getData()));
        }

        return responses;
    }

    @Override
    public TestResponse getResponseBySessionAndQuestionId(String sessionId, String questionId)
            throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("test_session_id", sessionId)
                .whereEqualTo("question_id", questionId)
                .get().get();

        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        if (documents.isEmpty()) {
            return null;
        }

        QueryDocumentSnapshot document = documents.get(0);
        return convertMapToTestResponse(document.getId(), document.getData());
    }

    @Override
    public String createResponse(TestResponse response) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();

        // Timestamp oluştur - GCP Firestore formatında
        Map<String, Object> timestamp = new HashMap<>();
        timestamp.put("seconds", System.currentTimeMillis() / 1000);
        timestamp.put("nanos", 0);
        response.setTimestamp(timestamp);

        Map<String, Object> responseData = convertTestResponseToMap(response);

        String documentId = firestore.collection(COLLECTION_NAME).document().getId();
        firebaseService.saveData(COLLECTION_NAME, documentId, responseData);

        return documentId;
    }

    @Override
    public String updateResponse(TestResponse response) throws ExecutionException, InterruptedException {
        // Güncellenmiş timestamp
        Map<String, Object> timestamp = new HashMap<>();
        timestamp.put("seconds", System.currentTimeMillis() / 1000);
        timestamp.put("nanos", 0);
        response.setTimestamp(timestamp);

        Map<String, Object> responseData = convertTestResponseToMap(response);
        return firebaseService.updateData(COLLECTION_NAME, response.getId(), responseData);
    }

    @Override
    public String deleteResponse(String id) throws ExecutionException, InterruptedException {
        return firebaseService.deleteData(COLLECTION_NAME, id);
    }

    @Override
    public List<TestResponse> getAllResponses() throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .get().get();

        List<TestResponse> responses = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            responses.add(convertMapToTestResponse(document.getId(), document.getData()));
        }

        return responses;
    }

    @Override
    public List<TestResponse> getResponsesByQuestionId(String questionId)
            throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("question_id", questionId)
                .get().get();

        List<TestResponse> responses = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            responses.add(convertMapToTestResponse(document.getId(), document.getData()));
        }

        return responses;
    }

    @SuppressWarnings("unchecked")
    private TestResponse convertMapToTestResponse(String id, Map<String, Object> data) {
        TestResponse response = new TestResponse();
        response.setId(id);
        response.setTest_session_id((String) data.get("test_session_id"));
        response.setPatient_id((String) data.get("patient_id"));
        response.setQuestion_id((String) data.get("question_id"));
        response.setScore_weight(data.get("score_weight")); // Direkt objeyi alıyoruz

        // Response'u map olarak al
        if (data.get("response") instanceof Map) {
            response.setResponse((Map<String, Object>) data.get("response"));
        } else {
            response.setResponse(new HashMap<>());
        }

        // Firestore timestamp
        if (data.get("timestamp") instanceof Map) {
            response.setTimestamp((Map<String, Object>) data.get("timestamp"));
        } else {
            // Varsayılan timestamp oluştur
            Map<String, Object> timestamp = new HashMap<>();
            timestamp.put("seconds", System.currentTimeMillis() / 1000);
            timestamp.put("nanos", 0);
            response.setTimestamp(timestamp);
        }

        return response;
    }

    private Map<String, Object> convertTestResponseToMap(TestResponse response) {
        Map<String, Object> data = new HashMap<>();
        data.put("test_session_id", response.getTest_session_id());
        data.put("patient_id", response.getPatient_id());
        data.put("question_id", response.getQuestion_id());
        data.put("score_weight", response.getScore_weight());
        data.put("response", response.getResponse());
        data.put("timestamp", response.getTimestamp());
        return data;
    }
}
