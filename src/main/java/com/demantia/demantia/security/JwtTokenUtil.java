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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtTokenUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

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
            logger.error("Error extracting username from token", e);
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
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            logger.error("Token validation error", e);
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
            logger.error("Error getting claims from token", e);
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
            logger.error("JWT token expired");
            throw e;
        } catch (MalformedJwtException e) {
            logger.error("Malformed JWT token");
            throw e;
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token");
            throw e;
        } catch (IllegalArgumentException e) {
            logger.error("JWT token compact of handler are invalid");
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error parsing JWT token", e);
            throw e;
        }
    }

    // Token'ın süresinin dolup dolmadığını kontrol eden metod
    private Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            logger.error("Error checking token expiration", e);
            return true; // Hata durumunda tokenı expired olarak kabul et
        }
    }

    // Token oluşturan yardımcı metod
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        long currentTimeMillis = System.currentTimeMillis();
        Date issuedAt = new Date(currentTimeMillis);
        Date expiryDate = new Date(currentTimeMillis + expiration * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(issuedAt)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // İmzalama anahtarını oluşturan metod
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
