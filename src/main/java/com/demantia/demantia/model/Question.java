package com.demantia.demantia.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private String id;
    private String category;
    private String question_text;
    private String question_type;
    private String related_group;
    // Options artık her iki formatta da olabilir: List veya Map
    private Object options; // List<String> veya Map<String, String>
    private String difficulty_level;
    private String followup_question_id; // Bazı sorularda mevcut
    private String imageUrl;

    // Options'ı List olarak almak için yardımcı metod
    @SuppressWarnings("unchecked")
    public List<String> getOptionsAsList() {
        if (options instanceof List) {
            return (List<String>) options;
        }
        return null;
    }

    // Options'ı Map olarak almak için yardımcı metod
    @SuppressWarnings("unchecked")
    public Map<String, String> getOptionsAsMap() {
        if (options instanceof Map) {
            return (Map<String, String>) options;
        }
        return null;
    }
}
