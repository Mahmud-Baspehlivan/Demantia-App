package com.demantia.demantia.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestResult {
    private String id;
    private String test_session_id;
    private String patient_id;
    private String recommendation;
    private Integer overall_score;
    private String risk_level;
    private String doctor_comments;
    // Eksik olan createdAt ve updatedAt alanlarını ekliyorum
    private String createdAt;
    private String updatedAt;
}
