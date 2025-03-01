package com.demantia.demantia.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAssignment {
    private String id;
    private String difficulty_level;
    private String education_level;
    private Integer max_age;
    private Integer min_age;
    private Integer priority_level;
    private String question_id;
    // test_session_id field'ını ekle
    private String test_session_id;
    private String risk_category;
    private LocalDateTime assignedAt;
    private LocalDateTime answeredAt;
    private int attemptCount;
}
