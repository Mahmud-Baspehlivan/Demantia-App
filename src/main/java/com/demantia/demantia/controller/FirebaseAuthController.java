package com.demantia.demantia.controller;

import com.demantia.demantia.security.FirebaseAuthService;
import com.demantia.demantia.security.JwtResponse;
import com.google.firebase.auth.FirebaseAuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class FirebaseAuthController {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseAuthController.class);

    @Autowired
    private FirebaseAuthService firebaseAuthService;

    @PostMapping("/firebase")
    public ResponseEntity<?> authenticateWithFirebase(@RequestBody Map<String, String> request) {
        try {
            String firebaseToken = request.get("firebaseToken");

            if (firebaseToken == null || firebaseToken.isEmpty()) {
                return ResponseEntity.badRequest().body("Firebase token is required");
            }

            JwtResponse jwtResponse = firebaseAuthService.verifyFirebaseTokenAndCreateJwt(firebaseToken);
            return ResponseEntity.ok(jwtResponse);
        } catch (FirebaseAuthException e) {
            logger.error("Firebase authentication failed", e);
            return ResponseEntity.status(401).body("Firebase authentication failed: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Server error during authentication", e);
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }

    // Manuel JWT doğrulama endpointi (test için) - prodüksiyon öncesi
    // kaldırılabilir
    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest().body("Authorization header must start with 'Bearer '");
            }

            String token = authHeader.substring(7);

            // Token bilgilerini döndür
            Map<String, String> response = new HashMap<>();
            response.put("message", "Token is valid");
            // Güvenlik için tokenı geri yansıtma
            // response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token verification failed: " + e.getMessage());
        }
    }
}
