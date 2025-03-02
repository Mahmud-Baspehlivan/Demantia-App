package com.demantia.demantia.controller;

import com.demantia.demantia.model.Patient;
import com.demantia.demantia.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        try {
            List<Patient> patients = patientService.getAllPatients();
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable String id) {
        try {
            Patient patient = patientService.getPatientById(id);
            if (patient != null) {
                return new ResponseEntity<>(patient, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/risk-category/{riskCategory}")
    public ResponseEntity<List<Patient>> getPatientsByRiskCategory(@PathVariable String riskCategory) {
        try {
            List<Patient> patients = patientService.getPatientsByRiskCategory(riskCategory);
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/cognitive-status/{cognitiveStatus}")
    public ResponseEntity<List<Patient>> getPatientsByCognitiveStatus(@PathVariable String cognitiveStatus) {
        try {
            List<Patient> patients = patientService.getPatientsByCognitiveStatus(cognitiveStatus);
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/age-range")
    public ResponseEntity<List<Patient>> getPatientsByAgeRange(
            @RequestParam("min") int minAge, @RequestParam("max") int maxAge) {
        try {
            List<Patient> patients = patientService.getPatientsByAgeRange(minAge, maxAge);
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/education-level/{educationLevel}")
    public ResponseEntity<List<Patient>> getPatientsByEducationLevel(@PathVariable String educationLevel) {
        try {
            List<Patient> patients = patientService.getPatientsByEducationLevel(educationLevel);
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<String> createPatient(@RequestBody Patient patient) {
        try {
            // Ensure role is patient
            patient.setRole("patient");

            String id = patientService.createPatient(patient);
            return new ResponseEntity<>(id, HttpStatus.CREATED);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/assess-risk")
    public ResponseEntity<String> assessRiskCategory(@PathVariable String id) {
        try {
            patientService.updateRiskCategory(id);
            return new ResponseEntity<>("Risk category updated", HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePatient(@PathVariable String id, @RequestBody Patient patient) {
        try {
            // Set the id from path variable
            patient.setId(id);

            // Ensure role remains patient
            patient.setRole("patient");

            String result = patientService.updatePatient(patient);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable String id) {
        try {
            String result = patientService.deletePatient(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
