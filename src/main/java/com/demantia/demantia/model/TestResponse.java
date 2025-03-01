package com.demantia.demantia.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestResponse {
    private String id;
    private String test_session_id;
    private String patient_id;
    private String question_id;
    private Object score_weight; // Bazen double, bazen string olabilir
    private Map<String, Object> response; // Farklı tiplerde değerler içerebilir
    private Map<String, Object> timestamp; // Firestore Timestamp'a karşılık gelir

    // score_weight'i double olarak almak için yardımcı metod
    public Double getScoreWeightAsDouble() {
        if (score_weight instanceof Number) {
            return ((Number) score_weight).doubleValue();
        } else if (score_weight instanceof String) {
            try {
                return Double.parseDouble((String) score_weight);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    // timestamp'ten long olarak unix timestamp almak için
    public Long getTimestampAsLong() {
        if (timestamp != null && timestamp.containsKey("seconds")) {
            Object seconds = timestamp.get("seconds");
            if (seconds instanceof Number) {
                return ((Number) seconds).longValue();
            }
        }
        return null;
    }
}
