package com.demantia.demantia.controller;

import com.demantia.demantia.security.FirebaseAuthService;
import com.demantia.demantia.security.JwtResponse;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class FirebaseAuthController {

    @Autowired
    private FirebaseAuthService firebaseAuthService;

    @PostMapping("/firebase")
    public ResponseEntity<?> authenticateWithFirebase(@RequestBody Map<String, String> request) {
        try {
            String firebaseToken = request.get("firebaseToken");

            if (firebaseToken == null || firebaseToken.isEmpty()) {
                return ResponseEntity.badRequest().body("Firebase token is required");
            }

            System.out.println("Received firebase token: "
                    + firebaseToken.substring(0, Math.min(10, firebaseToken.length())) + "...");

            JwtResponse jwtResponse = firebaseAuthService.verifyFirebaseTokenAndCreateJwt(firebaseToken);
            System.out.println("Generated JWT: "
                    + jwtResponse.getToken().substring(0, Math.min(10, jwtResponse.getToken().length())) + "...");
            System.out.println("User role: " + jwtResponse.getRole());
            System.out.println("User ID: " + jwtResponse.getId());

            return ResponseEntity.ok(jwtResponse);
        } catch (FirebaseAuthException e) {
            System.out.println("Firebase auth error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(401).body("Firebase authentication failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Server error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }

    // Manuel JWT doğrulama endpointi (test için)
    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest().body("Authorization header must start with 'Bearer '");
            }

            String token = authHeader.substring(7);
            System.out.println("Manually verifying token: " + token.substring(0, Math.min(10, token.length())) + "...");

            // Token bilgilerini döndür
            Map<String, String> response = new HashMap<>();
            response.put("message", "Token is valid");
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token verification failed: " + e.getMessage());
        }
    }
}
