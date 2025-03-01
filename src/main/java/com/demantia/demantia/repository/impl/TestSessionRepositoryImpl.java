package com.demantia.demantia.repository.impl;

import com.demantia.demantia.model.TestSession;
import com.demantia.demantia.repository.TestSessionRepository;
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
public class TestSessionRepositoryImpl implements TestSessionRepository {

    private static final String COLLECTION_NAME = "test_sessions";

    @Autowired
    private FirebaseService firebaseService;

    @Override
    public TestSession getSessionById(String id) throws ExecutionException, InterruptedException {
        Map<String, Object> data = firebaseService.getData(COLLECTION_NAME, id);
        if (data == null) {
            return null;
        }
        return convertMapToTestSession(id, data);
    }

    @Override
    public List<TestSession> getAllSessions() throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME).get().get();

        List<TestSession> sessions = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            sessions.add(convertMapToTestSession(document.getId(), document.getData()));
        }

        return sessions;
    }

    @Override
    public List<TestSession> getSessionsByPatientId(String patientId) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("patient_id", patientId)
                .get().get();

        List<TestSession> sessions = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            sessions.add(convertMapToTestSession(document.getId(), document.getData()));
        }

        return sessions;
    }

    @Override
    public List<TestSession> getSessionsByDoctorId(String doctorId) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("doctor_id", doctorId)
                .get().get();

        List<TestSession> sessions = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            sessions.add(convertMapToTestSession(document.getId(), document.getData()));
        }

        return sessions;
    }

    @Override
    public List<TestSession> getSessionsByStatus(String status) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("status", status)
                .get().get();

        List<TestSession> sessions = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            sessions.add(convertMapToTestSession(document.getId(), document.getData()));
        }

        return sessions;
    }

    @Override
    public String createSession(TestSession session) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        String timestamp = LocalDateTime.now().toString();
        session.setCreatedAt(timestamp);
        session.setUpdatedAt(timestamp);

        Map<String, Object> sessionData = convertTestSessionToMap(session);

        String documentId = firestore.collection(COLLECTION_NAME).document().getId();
        firebaseService.saveData(COLLECTION_NAME, documentId, sessionData);

        return documentId;
    }

    @Override
    public String updateSession(TestSession session) throws ExecutionException, InterruptedException {
        session.setUpdatedAt(LocalDateTime.now().toString());
        Map<String, Object> sessionData = convertTestSessionToMap(session);
        return firebaseService.updateData(COLLECTION_NAME, session.getId(), sessionData);
    }

    @Override
    public String deleteSession(String id) throws ExecutionException, InterruptedException {
        return firebaseService.deleteData(COLLECTION_NAME, id);
    }

    @Override
    public List<TestSession> getCompletedSessionsByPatientId(String patientId)
            throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("patient_id", patientId)
                .whereEqualTo("status", "completed")
                .get().get();

        List<TestSession> sessions = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            sessions.add(convertMapToTestSession(document.getId(), document.getData()));
        }

        return sessions;
    }

    @Override
    public TestSession getLatestSessionByPatientId(String patientId) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("patient_id", patientId)
                .orderBy("start_time", com.google.cloud.firestore.Query.Direction.DESCENDING)
                .limit(1)
                .get().get();

        if (querySnapshot.isEmpty()) {
            return null;
        }

        QueryDocumentSnapshot document = querySnapshot.getDocuments().get(0);
        return convertMapToTestSession(document.getId(), document.getData());
    }

    @Override
    public String completeSession(String sessionId) throws ExecutionException, InterruptedException {
        TestSession session = getSessionById(sessionId);
        if (session == null) {
            return null;
        }

        // Bitiş zamanını ayarla
        Map<String, Object> endTime = new HashMap<>();
        endTime.put("seconds", System.currentTimeMillis() / 1000);
        endTime.put("nanos", 0);
        session.setEnd_time(endTime);

        // Oturum durumunu "completed" olarak ayarla
        session.setStatus("completed");

        // Güncellenme zamanını güncelle
        session.setUpdatedAt(LocalDateTime.now().toString());

        // Firestore'da güncelle
        Map<String, Object> sessionData = convertTestSessionToMap(session);
        return firebaseService.updateData(COLLECTION_NAME, sessionId, sessionData);
    }

    @SuppressWarnings("unchecked")
    private TestSession convertMapToTestSession(String id, Map<String, Object> data) {
        TestSession session = new TestSession();
        session.setId(id);
        session.setDoctor_id((String) data.get("doctor_id"));
        session.setPatient_id((String) data.get("patient_id"));

        // Timestamp objeleri
        if (data.get("start_time") instanceof Map) {
            session.setStart_time((Map<String, Object>) data.get("start_time"));
        }

        if (data.get("end_time") instanceof Map) {
            session.setEnd_time((Map<String, Object>) data.get("end_time"));
        }

        session.setStatus((String) data.get("status"));
        session.setResult_summary((String) data.get("result_summary"));
        session.setRecommendation((String) data.get("recommendation"));
        session.setScore(data.get("score"));
        session.setCreatedAt((String) data.get("createdAt"));
        session.setUpdatedAt((String) data.get("updatedAt"));

        return session;
    }

    private Map<String, Object> convertTestSessionToMap(TestSession session) {
        Map<String, Object> data = new HashMap<>();
        data.put("doctor_id", session.getDoctor_id());
        data.put("patient_id", session.getPatient_id());
        data.put("start_time", session.getStart_time());
        data.put("end_time", session.getEnd_time());
        data.put("status", session.getStatus());
        data.put("result_summary", session.getResult_summary());
        data.put("recommendation", session.getRecommendation());
        data.put("score", session.getScore());
        data.put("createdAt", session.getCreatedAt());
        data.put("updatedAt", session.getUpdatedAt());
        return data;
    }
}
