package com.demantia.demantia.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.nio.charset.StandardCharsets;

@Component
public class JwtTokenUtil {

    // JWT token geçerlilik süresi (saniye)
    @Value("${jwt.expiration:86400}")
    private long expiration;

    // JWT için secret key
    @Value("${jwt.secret:demantiaSecretKey12345678901234567890ABCDEFGHIJKL}")
    private String secret;

    // Token'dan kullanıcı adını çıkaran metod
    public String getUsernameFromToken(String token) {
        try {
            return getClaimFromToken(token, Claims::getSubject);
        } catch (Exception e) {
            System.out.println("Error extracting username from token: " + e.getMessage());
            throw e;
        }
    }

    // Token'dan son geçerlilik tarihini çıkaran metod
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Token'ı doğrulayan metod
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(token);
            boolean isValid = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
            System.out.println("Token validation - Username match: " + username.equals(userDetails.getUsername()) +
                    ", Not expired: " + !isTokenExpired(token));
            return isValid;
        } catch (Exception e) {
            System.out.println("Token validation error: " + e.getMessage());
            return false;
        }
    }

    // Token oluşturan metod
    public String generateToken(UserDetails userDetails, String userId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        return doGenerateToken(claims, userDetails.getUsername());
    }

    // Generic bir claims çıkarma metodu
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = getAllClaimsFromToken(token);
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            System.out.println("Error getting claims from token: " + e.getMessage());
            throw e;
        }
    }

    // Token'dan tüm claim'leri çıkaran metod
    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token expired: " + e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            System.out.println("Malformed JWT token: " + e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT token: " + e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            System.out.println("JWT token compact of handler are invalid: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("Unexpected error parsing JWT token: " + e.getMessage());
            throw e;
        }
    }

    // Token'ın süresinin dolup dolmadığını kontrol eden metod
    private Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            boolean isExpired = expiration.before(new Date());
            if (isExpired) {
                System.out.println("Token expired. Expiration: " + expiration + ", Current: " + new Date());
            }
            return isExpired;
        } catch (Exception e) {
            System.out.println("Error checking token expiration: " + e.getMessage());
            return true; // Hata durumunda tokenı expired olarak kabul et
        }
    }

    // Token oluşturan yardımcı metod
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        long currentTimeMillis = System.currentTimeMillis();
        Date issuedAt = new Date(currentTimeMillis);
        Date expiryDate = new Date(currentTimeMillis + expiration * 1000);

        System.out.println("Generating token for subject: " + subject);
        System.out.println("Token issued at: " + issuedAt);
        System.out.println("Token expiry: " + expiryDate);

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(issuedAt)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();

        System.out.println("Generated token: " + token.substring(0, Math.min(10, token.length())) + "...");
        return token;
    }

    // İmzalama anahtarını oluşturan metod
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        System.out.println("Secret key length: " + keyBytes.length + " bytes");
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
