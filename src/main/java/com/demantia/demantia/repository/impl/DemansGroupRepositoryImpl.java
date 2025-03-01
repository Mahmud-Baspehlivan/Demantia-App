package com.demantia.demantia.repository.impl;

import com.demantia.demantia.model.DemansGroup;
import com.demantia.demantia.repository.DemansGroupRepository;
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
public class DemansGroupRepositoryImpl implements DemansGroupRepository {

    private static final String COLLECTION_NAME = "demans_groups";

    @Autowired
    private FirebaseService firebaseService;

    @Override
    public DemansGroup getGroupById(String id) throws ExecutionException, InterruptedException {
        Map<String, Object> data = firebaseService.getData(COLLECTION_NAME, id);
        if (data == null) {
            return null;
        }
        return convertMapToDemansGroup(id, data);
    }

    @Override
    public List<DemansGroup> getAllGroups() throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME).get().get();

        List<DemansGroup> groups = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            groups.add(convertMapToDemansGroup(document.getId(), document.getData()));
        }

        return groups;
    }

    @Override
    public DemansGroup getGroupByAgeRange(String ageRange) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("age_range", ageRange)
                .get().get();

        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        if (documents.isEmpty()) {
            return null;
        }

        QueryDocumentSnapshot document = documents.get(0);
        return convertMapToDemansGroup(document.getId(), document.getData());
    }

    @Override
    public String createGroup(DemansGroup group) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        Map<String, Object> groupData = convertDemansGroupToMap(group);

        String documentId = firestore.collection(COLLECTION_NAME).document().getId();
        firebaseService.saveData(COLLECTION_NAME, documentId, groupData);

        return documentId;
    }

    @Override
    public String updateGroup(DemansGroup group) throws ExecutionException, InterruptedException {
        Map<String, Object> groupData = convertDemansGroupToMap(group);
        return firebaseService.updateData(COLLECTION_NAME, group.getId(), groupData);
    }

    @Override
    public String deleteGroup(String id) throws ExecutionException, InterruptedException {
        return firebaseService.deleteData(COLLECTION_NAME, id);
    }

    @SuppressWarnings("unchecked")
    private DemansGroup convertMapToDemansGroup(String id, Map<String, Object> data) {
        DemansGroup group = new DemansGroup();
        group.setId(id);
        group.setAge_range((String) data.get("age_range"));
        group.setGroup_name((String) data.get("group_name"));

        // Listeler için güvenli dönüşümler
        if (data.get("gender") instanceof List) {
            group.setGender((List<String>) data.get("gender"));
        } else {
            group.setGender(new ArrayList<>());
        }

        if (data.get("cognitive_status") instanceof List) {
            group.setCognitive_status((List<String>) data.get("cognitive_status"));
        } else {
            group.setCognitive_status(new ArrayList<>());
        }

        if (data.get("occupation_level") instanceof List) {
            group.setOccupation_level((List<String>) data.get("occupation_level"));
        } else {
            group.setOccupation_level(new ArrayList<>());
        }

        if (data.get("risk_factors") instanceof List) {
            group.setRisk_factors((List<String>) data.get("risk_factors"));
        } else {
            group.setRisk_factors(new ArrayList<>());
        }

        if (data.get("social_activity") instanceof List) {
            group.setSocial_activity((List<String>) data.get("social_activity"));
        } else {
            group.setSocial_activity(new ArrayList<>());
        }

        if (data.get("education_levels") instanceof List) {
            group.setEducation_levels((List<String>) data.get("education_levels"));
        } else {
            group.setEducation_levels(new ArrayList<>());
        }

        return group;
    }

    private Map<String, Object> convertDemansGroupToMap(DemansGroup group) {
        Map<String, Object> data = new HashMap<>();
        data.put("age_range", group.getAge_range());
        data.put("group_name", group.getGroup_name());
        data.put("gender", group.getGender());
        data.put("cognitive_status", group.getCognitive_status());
        data.put("occupation_level", group.getOccupation_level());
        data.put("risk_factors", group.getRisk_factors());
        data.put("social_activity", group.getSocial_activity());
        data.put("education_levels", group.getEducation_levels());
        return data;
    }
}
