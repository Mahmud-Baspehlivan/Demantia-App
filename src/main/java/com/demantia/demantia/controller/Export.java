package com.demantia.demantia.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class Export {

    @GetMapping("/exportFirestore")
    public ResponseEntity<?> exportFirestore() {
        try {
            Firestore firestore = FirestoreClient.getFirestore();
            Iterable<CollectionReference> collections = firestore.listCollections();
            List<Map<String, Object>> allData = new ArrayList<>();

            for (CollectionReference collection : collections) {
                Map<String, Object> collectionMap = new HashMap<>();
                collectionMap.put("collection_name", collection.getId());

                List<Map<String, Object>> documentsList = new ArrayList<>();
                ApiFuture<QuerySnapshot> query = collection.get();
                for (QueryDocumentSnapshot document : query.get().getDocuments()) {
                    Map<String, Object> documentData = document.getData();
                    documentData.put("id", document.getId()); // Belge ID'sini ekle
                    documentsList.add(documentData);
                }

                collectionMap.put("documents", documentsList);
                allData.add(collectionMap);
            }

            return ResponseEntity.ok(allData);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error exporting Firestore: " + e.getMessage());
        }
    }
}
