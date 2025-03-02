package com.demantia.demantia.controller;

import com.demantia.demantia.security.JwtResponse;
import com.demantia.demantia.security.JwtTokenUtil;
import com.demantia.demantia.security.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Test amaçlı controller - sadece geliştirme ortamında kullanılmalıdır
 */
@RestController
@RequestMapping("/api/auth/test")
public class TestAuthController {

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * Test kullanıcıları için manuel token oluşturur.
     * Bu endpoint sadece geliştirme aşamasında kullanılmalıdır.
     */
    @GetMapping("/token/{email}")
    public ResponseEntity<?> createTestToken(@PathVariable String email) {
        try {
            // Kullanıcı bilgilerini yükle
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Kullanıcı Firebase'de kaydedilmemiş ise
            if (userDetails == null) {
                return ResponseEntity.badRequest().body("Kullanıcı bulunamadı: " + email);
            }

            // Kullanıcı detaylarını al
            String userId = "user_" + System.currentTimeMillis();
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                    .orElse("USER");

            // Token oluştur
            String token = jwtTokenUtil.generateToken(userDetails, userId, role);

            return ResponseEntity.ok(new JwtResponse(token, role, userId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Token oluşturma hatası: " + e.getMessage());
        }
    }

    /**
     * Token decode edip içindeki bilgileri döndürür
     */
    @PostMapping("/decode")
    public ResponseEntity<?> decodeToken(@RequestBody Map<String, String> requestBody) {
        try {
            String token = requestBody.get("token");
            if (token == null || token.isEmpty()) {
                return ResponseEntity.badRequest().body("Token gerekli");
            }

            // Token'dan bilgileri çıkart
            Map<String, Object> response = new HashMap<>();

            try {
                String username = jwtTokenUtil.getUsernameFromToken(token);
                response.put("username", username);
            } catch (Exception e) {
                response.put("username_error", e.getMessage());
            }

            try {
                response.put("expiration", jwtTokenUtil.getExpirationDateFromToken(token));
            } catch (Exception e) {
                response.put("expiration_error", e.getMessage());
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Token decode hatası: " + e.getMessage());
        }
    }
}
