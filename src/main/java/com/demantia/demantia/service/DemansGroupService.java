package com.demantia.demantia.service;

import com.demantia.demantia.model.DemansGroup;
import com.demantia.demantia.model.Patient;
import com.demantia.demantia.repository.DemansGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class DemansGroupService {

    @Autowired
    private DemansGroupRepository demansGroupRepository;

    public DemansGroup getGroupById(String id) throws ExecutionException, InterruptedException {
        return demansGroupRepository.getGroupById(id);
    }

    public List<DemansGroup> getAllGroups() throws ExecutionException, InterruptedException {
        return demansGroupRepository.getAllGroups();
    }

    public DemansGroup getGroupByAgeRange(String ageRange) throws ExecutionException, InterruptedException {
        return demansGroupRepository.getGroupByAgeRange(ageRange);
    }

    public String createGroup(DemansGroup group) throws ExecutionException, InterruptedException {
        return demansGroupRepository.createGroup(group);
    }

    public String updateGroup(DemansGroup group) throws ExecutionException, InterruptedException {
        return demansGroupRepository.updateGroup(group);
    }

    public String deleteGroup(String id) throws ExecutionException, InterruptedException {
        return demansGroupRepository.deleteGroup(id);
    }

    // İş mantığı için yardımcı metodlar

    public DemansGroup getGroupForPatient(Patient patient) throws ExecutionException, InterruptedException {
        int age = patient.getAge() != null ? patient.getAge() : 0;

        // Yaş aralığını belirle
        String ageRange;
        if (age >= 80) {
            ageRange = "80+";
        } else if (age >= 70) {
            ageRange = "70-79";
        } else if (age >= 60) {
            ageRange = "60-69";
        } else {
            ageRange = "50-59";
        }

        return getGroupByAgeRange(ageRange);
    }

    public boolean isPatientInRiskGroup(Patient patient) throws ExecutionException, InterruptedException {
        DemansGroup group = getGroupForPatient(patient);
        if (group == null) {
            return false;
        }

        String cognitiveStatus = patient.getCognitive_status();
        return group.getCognitive_status().contains(cognitiveStatus);
    }
}
