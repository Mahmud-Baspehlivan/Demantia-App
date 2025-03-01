package com.demantia.demantia.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemansGroup {
    private String id;
    private String age_range;
    private String group_name;
    private List<String> gender;
    private List<String> cognitive_status;
    private List<String> occupation_level;
    private List<String> risk_factors;
    private List<String> social_activity;
    private List<String> education_levels;
}
