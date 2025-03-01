package com.demantia.demantia.repository.impl;

import com.demantia.demantia.model.QuestionAssignment;
import com.demantia.demantia.repository.QuestionAssignmentRepository;
import com.demantia.demantia.service.FirebaseService;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public class QuestionAssignmentRepositoryImpl implements QuestionAssignmentRepository {

    private static final String COLLECTION_NAME = "question_assignment";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Autowired
    private FirebaseService firebaseService;

    @Override
    public QuestionAssignment getAssignmentById(String id) throws ExecutionException, InterruptedException {
        Map<String, Object> data = firebaseService.getData(COLLECTION_NAME, id);
        if (data == null) {
            return null;
        }
        return convertMapToQuestionAssignment(id, data);
    }

    @Override
    public List<QuestionAssignment> getAssignmentsBySessionId(String testSessionId)
            throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("test_session_id", testSessionId)
                .get().get();

        List<QuestionAssignment> assignments = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            assignments.add(convertMapToQuestionAssignment(document.getId(), document.getData()));
        }

        return assignments;
    }

    @Override
    public List<QuestionAssignment> getAssignmentsByQuestionId(String questionId)
            throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("question_id", questionId)
                .get().get();

        List<QuestionAssignment> assignments = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            assignments.add(convertMapToQuestionAssignment(document.getId(), document.getData()));
        }

        return assignments;
    }

    @Override
    public String createAssignment(QuestionAssignment assignment) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        Map<String, Object> assignmentData = convertQuestionAssignmentToMap(assignment);

        String documentId = firestore.collection(COLLECTION_NAME).document().getId();
        firebaseService.saveData(COLLECTION_NAME, documentId, assignmentData);

        return documentId;
    }

    @Override
    public String updateAssignment(QuestionAssignment assignment) throws ExecutionException, InterruptedException {
        Map<String, Object> assignmentData = convertQuestionAssignmentToMap(assignment);
        return firebaseService.updateData(COLLECTION_NAME, assignment.getId(), assignmentData);
    }

    @Override
    public String deleteAssignment(String id) throws ExecutionException, InterruptedException {
        return firebaseService.deleteData(COLLECTION_NAME, id);
    }

    @Override
    public List<QuestionAssignment> getAssignmentsByDifficultyLevel(String difficultyLevel)
            throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("difficulty_level", difficultyLevel)
                .get().get();

        List<QuestionAssignment> assignments = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            assignments.add(convertMapToQuestionAssignment(document.getId(), document.getData()));
        }

        return assignments;
    }

    @Override
    public List<QuestionAssignment> getAssignmentsForAgeGroup(int age)
            throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereLessThanOrEqualTo("max_age", age)
                .whereGreaterThanOrEqualTo("min_age", age)
                .get().get();

        List<QuestionAssignment> assignments = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            assignments.add(convertMapToQuestionAssignment(document.getId(), document.getData()));
        }

        return assignments;
    }

    @Override
    public List<QuestionAssignment> getAssignmentsByEducationLevel(String educationLevel)
            throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("education_level", educationLevel)
                .get().get();

        List<QuestionAssignment> assignments = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            assignments.add(convertMapToQuestionAssignment(document.getId(), document.getData()));
        }

        return assignments;
    }

    @Override
    public List<QuestionAssignment> getAssignmentsByRiskCategory(String riskCategory)
            throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("risk_category", riskCategory)
                .get().get();

        List<QuestionAssignment> assignments = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            assignments.add(convertMapToQuestionAssignment(document.getId(), document.getData()));
        }

        return assignments;
    }

    @Override
    public List<QuestionAssignment> getAllAssignments()
            throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .get().get();

        List<QuestionAssignment> assignments = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            assignments.add(convertMapToQuestionAssignment(document.getId(), document.getData()));
        }

        return assignments;
    }

    private QuestionAssignment convertMapToQuestionAssignment(String id, Map<String, Object> data) {
        QuestionAssignment assignment = new QuestionAssignment();
        assignment.setId(id);
        assignment.setDifficulty_level((String) data.get("difficulty_level"));
        assignment.setEducation_level((String) data.get("education_level"));

        // Handle integer values
        Long maxAge = (Long) data.get("max_age");
        if (maxAge != null) {
            assignment.setMax_age(maxAge.intValue());
        }

        Long minAge = (Long) data.get("min_age");
        if (minAge != null) {
            assignment.setMin_age(minAge.intValue());
        }

        Long priorityLevel = (Long) data.get("priority_level");
        if (priorityLevel != null) {
            assignment.setPriority_level(priorityLevel.intValue());
        }

        assignment.setQuestion_id((String) data.get("question_id"));
        assignment.setTest_session_id((String) data.get("test_session_id"));
        assignment.setRisk_category((String) data.get("risk_category"));

        // Handle LocalDateTime objects
        String assignedAtStr = (String) data.get("assignedAt");
        if (assignedAtStr != null) {
            assignment.setAssignedAt(LocalDateTime.parse(assignedAtStr, formatter));
        }

        String answeredAtStr = (String) data.get("answeredAt");
        if (answeredAtStr != null) {
            assignment.setAnsweredAt(LocalDateTime.parse(answeredAtStr, formatter));
        }

        Long attemptCountLong = (Long) data.get("attemptCount");
        if (attemptCountLong != null) {
            assignment.setAttemptCount(attemptCountLong.intValue());
        }

        return assignment;
    }

    private Map<String, Object> convertQuestionAssignmentToMap(QuestionAssignment assignment) {
        Map<String, Object> data = new HashMap<>();
        data.put("difficulty_level", assignment.getDifficulty_level());
        data.put("education_level", assignment.getEducation_level());
        data.put("max_age", assignment.getMax_age());
        data.put("min_age", assignment.getMin_age());
        data.put("priority_level", assignment.getPriority_level());
        data.put("question_id", assignment.getQuestion_id());
        data.put("test_session_id", assignment.getTest_session_id());
        data.put("risk_category", assignment.getRisk_category());

        // Handle LocalDateTime objects
        LocalDateTime assignedAt = assignment.getAssignedAt();
        if (assignedAt != null) {
            data.put("assignedAt", assignedAt.format(formatter));
        }

        LocalDateTime answeredAt = assignment.getAnsweredAt();
        if (answeredAt != null) {
            data.put("answeredAt", answeredAt.format(formatter));
        }

        data.put("attemptCount", assignment.getAttemptCount());
        return data;
    }
}
