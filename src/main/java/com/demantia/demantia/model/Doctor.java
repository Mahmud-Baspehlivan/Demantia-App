package com.demantia.demantia.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Doctor extends User {
    private String specialization;
    private String hospital;

    public Doctor() {
        setRole("doctor");
    }
}
