package com.demantia.demantia.repository.impl;

import com.demantia.demantia.model.Question;
import com.demantia.demantia.repository.QuestionRepository;
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
public class QuestionRepositoryImpl implements QuestionRepository {

    private static final String COLLECTION_NAME = "questions";

    @Autowired
    private FirebaseService firebaseService;

    @Override
    public Question getQuestionById(String id) throws ExecutionException, InterruptedException {
        Map<String, Object> data = firebaseService.getData(COLLECTION_NAME, id);
        if (data == null) {
            return null;
        }
        return convertMapToQuestion(id, data);
    }

    @Override
    public List<Question> getAllQuestions() throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME).get().get();

        List<Question> questions = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            questions.add(convertMapToQuestion(document.getId(), document.getData()));
        }

        return questions;
    }

    @Override
    public List<Question> getQuestionsByCategory(String category) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("category", category)
                .get().get();

        List<Question> questions = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            questions.add(convertMapToQuestion(document.getId(), document.getData()));
        }

        return questions;
    }

    @Override
    public List<Question> getQuestionsByIds(List<String> ids) throws ExecutionException, InterruptedException {
        List<Question> questions = new ArrayList<>();
        for (String id : ids) {
            Question question = getQuestionById(id);
            if (question != null) {
                questions.add(question);
            }
        }
        return questions;
    }

    @Override
    public String createQuestion(Question question) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        Map<String, Object> questionData = convertQuestionToMap(question);

        String documentId = firestore.collection(COLLECTION_NAME).document().getId();
        firebaseService.saveData(COLLECTION_NAME, documentId, questionData);

        return documentId;
    }

    @Override
    public String updateQuestion(Question question) throws ExecutionException, InterruptedException {
        Map<String, Object> questionData = convertQuestionToMap(question);
        return firebaseService.updateData(COLLECTION_NAME, question.getId(), questionData);
    }

    @Override
    public String deleteQuestion(String id) throws ExecutionException, InterruptedException {
        return firebaseService.deleteData(COLLECTION_NAME, id);
    }

    @Override
    public List<Question> getQuestionsByRelatedGroup(String relatedGroup)
            throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("related_group", relatedGroup)
                .get().get();

        List<Question> questions = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            questions.add(convertMapToQuestion(document.getId(), document.getData()));
        }

        return questions;
    }

    @Override
    public List<Question> getQuestionsByDifficultyLevel(String difficultyLevel)
            throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("difficulty_level", difficultyLevel)
                .get().get();

        List<Question> questions = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            questions.add(convertMapToQuestion(document.getId(), document.getData()));
        }

        return questions;
    }

    private Question convertMapToQuestion(String id, Map<String, Object> data) {
        Question question = new Question();
        question.setId(id);
        question.setCategory((String) data.get("category"));
        question.setQuestion_text((String) data.get("question_text"));
        question.setQuestion_type((String) data.get("question_type"));
        question.setRelated_group((String) data.get("related_group"));
        question.setFollowup_question_id((String) data.get("followup_question_id"));

        // Options alanı için güvenli dönüşüm - options List veya Map olabilir
        Object optionsObj = data.get("options");
        // Options objesini olduğu gibi ata (hem List hem Map desteklendiği için)
        question.setOptions(optionsObj);

        // Difficulty level'ı direkt olarak String'e çeviriyoruz
        Object difficultyObj = data.get("difficulty_level");
        if (difficultyObj != null) {
            question.setDifficulty_level(String.valueOf(difficultyObj));
        } else {
            question.setDifficulty_level("medium"); // Varsayılan değer
        }

        question.setImageUrl((String) data.get("imageUrl"));
        return question;
    }

    private Map<String, Object> convertQuestionToMap(Question question) {
        Map<String, Object> data = new HashMap<>();
        data.put("category", question.getCategory());
        data.put("question_text", question.getQuestion_text());
        data.put("question_type", question.getQuestion_type());
        data.put("related_group", question.getRelated_group());
        data.put("options", question.getOptions()); // Direkt options objesini ekle
        data.put("difficulty_level", question.getDifficulty_level());
        data.put("followup_question_id", question.getFollowup_question_id());
        data.put("imageUrl", question.getImageUrl());
        return data;
    }
}
