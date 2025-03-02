package com.demantia.demantia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/public/test")
    public ResponseEntity<?> publicTest() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a public endpoint - no authentication required");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public ResponseEntity<?> protectedTest(Principal principal) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a protected endpoint");
        response.put("username", principal.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctor/test")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> doctorTest(Principal principal) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a doctor-only endpoint");
        response.put("username", principal.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/test")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<?> patientTest(Principal principal) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a patient-only endpoint");
        response.put("username", principal.getName());
        return ResponseEntity.ok(response);
    }
}
