package com.demantia.demantia.controller;

import com.demantia.demantia.model.User;
import com.demantia.demantia.security.JwtRequest;
import com.demantia.demantia.security.JwtResponse;
import com.demantia.demantia.security.JwtTokenUtil;
import com.demantia.demantia.security.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        try {
            System.out.println("=== LOGIN ATTEMPT ===");
            System.out.println("Username: " + authenticationRequest.getUsername());

            // Step 1: Authenticate user - bypassed for testing
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

            // Step 2: Load user details
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            System.out.println("User loaded: " + userDetails.getUsername());

            // Step 3: Get user information
            User user = jwtUserDetailsService.getUserByEmail(authenticationRequest.getUsername());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User data could not be retrieved");
            }
            System.out.println("User found: " + user.getName() + " (Role: " + user.getRole() + ")");

            // Step 4: Generate token
            String token = jwtTokenUtil.generateToken(userDetails, user.getId(), user.getRole());
            System.out.println("Token generated successfully");

            // Step 5: Return response
            return ResponseEntity.ok(new JwtResponse(token, user.getRole(), user.getId()));
        } catch (Exception e) {
            System.err.println("Authentication error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication failed: " + e.getMessage()));
        }
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Auth endpoint is working");
        response.put("status", "OK");
        return ResponseEntity.ok(response);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            System.out.println("TEST MODE: Authentication bypass enabled - all passwords accepted");

            // Username check only
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
            if (userDetails == null) {
                throw new BadCredentialsException("Invalid username");
            }

            // Skip password validation for testing
            // Real implementation would use AuthenticationManager here
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        } catch (Exception e) {
            throw new Exception("AUTHENTICATION_ERROR: " + e.getMessage(), e);
        }
    }
}
