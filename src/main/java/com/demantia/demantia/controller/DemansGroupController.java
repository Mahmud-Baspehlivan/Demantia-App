package com.demantia.demantia.controller;

import com.demantia.demantia.model.DemansGroup;
import com.demantia.demantia.model.Patient;
import com.demantia.demantia.service.DemansGroupService;
import com.demantia.demantia.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/demans-groups")
@CrossOrigin(origins = "*")
public class DemansGroupController {

    @Autowired
    private DemansGroupService demansGroupService;

    @Autowired
    private PatientService patientService;

    @GetMapping("/{id}")
    public ResponseEntity<DemansGroup> getGroupById(@PathVariable String id) {
        try {
            DemansGroup group = demansGroupService.getGroupById(id);
            if (group != null) {
                return new ResponseEntity<>(group, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<DemansGroup>> getAllGroups() {
        try {
            List<DemansGroup> groups = demansGroupService.getAllGroups();
            return new ResponseEntity<>(groups, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/age-range/{ageRange}")
    public ResponseEntity<DemansGroup> getGroupByAgeRange(@PathVariable String ageRange) {
        try {
            DemansGroup group = demansGroupService.getGroupByAgeRange(ageRange);
            if (group != null) {
                return new ResponseEntity<>(group, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<DemansGroup> getGroupForPatient(@PathVariable String patientId) {
        try {
            Patient patient = patientService.getPatientById(patientId);
            if (patient == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            DemansGroup group = demansGroupService.getGroupForPatient(patient);
            if (group != null) {
                return new ResponseEntity<>(group, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/patient/{patientId}/risk-check")
    public ResponseEntity<Boolean> isPatientInRiskGroup(@PathVariable String patientId) {
        try {
            Patient patient = patientService.getPatientById(patientId);
            if (patient == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            boolean isInRiskGroup = demansGroupService.isPatientInRiskGroup(patient);
            return new ResponseEntity<>(isInRiskGroup, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<String> createGroup(@RequestBody DemansGroup group) {
        try {
            String id = demansGroupService.createGroup(group);
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateGroup(@PathVariable String id, @RequestBody DemansGroup group) {
        try {
            group.setId(id);
            String result = demansGroupService.updateGroup(group);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGroup(@PathVariable String id) {
        try {
            String result = demansGroupService.deleteGroup(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
