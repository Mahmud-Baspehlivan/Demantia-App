package com.demantia.demantia.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String path = request.getRequestURI();

        // Bazı endpointleri filtreden muaf tut
        if ((path != null && (path.startsWith("/api/auth/") || path.startsWith("/api/public/")))) {
            System.out.println("DEBUG: Bypassing JWT filter for path: " + path);
            chain.doFilter(request, response);
            return;
        }

        final String requestTokenHeader = request.getHeader("Authorization");
        System.out.println("DEBUG: Processing request: " + path + " | Auth header: "
                + (requestTokenHeader != null ? requestTokenHeader : "Not present"));

        String username = null;
        String jwtToken = null;

        // JWT Token, "Bearer token" şeklinde geliyorsa işle
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                // Debug için token yapısını göster
                debugJwtToken(jwtToken);

                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                System.out.println("DEBUG: Extracted username from token: " + username);
            } catch (IllegalArgumentException e) {
                System.out.println("DEBUG: Unable to get JWT Token: " + e.getMessage());
            } catch (ExpiredJwtException e) {
                System.out.println("DEBUG: JWT Token has expired: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"JWT token expired\",\"message\":\"Token has expired\"}");
                return;
            } catch (Exception e) {
                System.out.println("DEBUG: Error processing token: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("DEBUG: JWT Token does not begin with Bearer String or is missing");
        }

        // SecurityContext'te kimlik doğrulaması yapılmamışsa yap
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                System.out.println("DEBUG: Loading UserDetails for: " + username);
                UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

                // Token geçerliyse Spring Security konfigürasyonu
                System.out.println("DEBUG: Validating token for user: " + username);
                if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Kimlik doğrulama bilgisini SecurityContext'e yerleştir
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("DEBUG: Authentication successful for: " + username + " with authorities: " +
                            userDetails.getAuthorities());
                } else {
                    System.out.println("DEBUG: Token validation failed for: " + username);
                }
            } catch (Exception e) {
                System.out.println("DEBUG: Authentication error: " + e.getMessage());
                e.printStackTrace();
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * Debug için JWT token yapısını göster
     */
    private void debugJwtToken(String token) {
        try {
            System.out.println("DEBUG: Analyzing token: " + token.substring(0, Math.min(20, token.length())) + "...");

            // Token'ı parçalara ayır
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                System.out.println("DEBUG: Invalid token structure - expected 3 parts but found " + parts.length);
                return;
            }

            // Header ve payload'ı decode et
            String decodedHeader = new String(Base64.getDecoder().decode(parts[0]));
            String decodedPayload = new String(Base64.getDecoder().decode(parts[1]));

            System.out.println("DEBUG: Token header: " + decodedHeader);
            System.out.println("DEBUG: Token payload: " + decodedPayload);
        } catch (Exception e) {
            System.out.println("DEBUG: Error analyzing token: " + e.getMessage());
        }
    }
}
