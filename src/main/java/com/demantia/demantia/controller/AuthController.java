package com.demantia.demantia.controller;

import com.demantia.demantia.model.User;
import com.demantia.demantia.security.JwtRequest;
import com.demantia.demantia.security.JwtResponse;
import com.demantia.demantia.security.JwtTokenUtil;
import com.demantia.demantia.security.JwtUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        try {
            logger.info("Login attempt for user: {}", authenticationRequest.getUsername());

            // Step 1: Authenticate user
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

            // Step 2: Load user details
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

            // Step 3: Get user information
            User user = userDetailsService.getUserByEmail(authenticationRequest.getUsername());
            if (user == null) {
                logger.error("User data could not be retrieved for: {}", authenticationRequest.getUsername());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User data could not be retrieved");
            }

            // Step 4: Generate token
            final String token = jwtTokenUtil.generateToken(userDetails, user.getId(), user.getRole());
            logger.info("Token generated for user: {}", authenticationRequest.getUsername());

            // Step 5: Return response
            return ResponseEntity.ok(new JwtResponse(token, user.getRole(), user.getId()));
        } catch (Exception e) {
            logger.error("Authentication error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication failed"));
        }
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Auth endpoint is working");
        response.put("status", "OK");
        return ResponseEntity.ok(response);
    }

    /**
     * Kullanıcı adı ve şifre ile kimlik doğrulama yapar
     */
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        } catch (Exception e) {
            throw new Exception("AUTHENTICATION_ERROR", e);
        }
    }
}
