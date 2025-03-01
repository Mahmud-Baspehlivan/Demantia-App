package com.demantia.demantia.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Patient extends User {
    private Integer age;
    private String gender;
    private String cognitive_status;
    private String education_level;
    private String risk_category;

    public Patient() {
        setRole("patient");
    }
}
