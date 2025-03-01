package com.demantia.demantia.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestSession {
    private String id;
    private String doctor_id;
    private String patient_id;
    private Map<String, Object> start_time; // Firestore Timestamp
    private Map<String, Object> end_time; // Firestore Timestamp, null olabilir
    private String status; // "in_progress", "completed" vb.
    private String result_summary; // null olabilir
    private String recommendation; // null olabilir
    private Object score; // null olabilir
    // Zaman bilgileri ekledim
    private String createdAt;
    private String updatedAt;

    // start_time'ı long olarak almak için
    public Long getStartTimeAsLong() {
        if (start_time != null && start_time.containsKey("seconds")) {
            Object seconds = start_time.get("seconds");
            if (seconds instanceof Number) {
                return ((Number) seconds).longValue();
            }
        }
        return null;
    }

    // end_time'ı long olarak almak için
    public Long getEndTimeAsLong() {
        if (end_time != null && end_time.containsKey("seconds")) {
            Object seconds = end_time.get("seconds");
            if (seconds instanceof Number) {
                return ((Number) seconds).longValue();
            }
        }
        return null;
    }
}
