package com.demantia.demantia.config;

import com.demantia.demantia.security.JwtAuthenticationEntryPoint;
import com.demantia.demantia.security.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

        @Autowired
        private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

        @Autowired
        private JwtRequestFilter jwtRequestFilter;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(authorize -> authorize
                                                // Auth ve public endpoint'lere herkesin erişimine izin ver
                                                .requestMatchers(new AntPathRequestMatcher("/api/auth/**")).permitAll()
                                                .requestMatchers(new AntPathRequestMatcher("/api/public/**"))
                                                .permitAll()

                                                // Swagger UI ve API dokümantasyon sayfalarına erişime izin ver
                                                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**"))
                                                .permitAll()
                                                .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**"))
                                                .permitAll()

                                                // Diğer tüm isteklerin kimlik doğrulaması yapılmış olması gerekir
                                                .anyRequest().authenticated())

                                // Kimlik doğrulaması hatası durumunda özel hata mesajı sağlayan entry point
                                .exceptionHandling(
                                                exceptionHandling -> exceptionHandling
                                                                .authenticationEntryPoint(jwtAuthenticationEntryPoint))

                                // Session yönetimini stateless yap (JWT kullanımı için gerekli)
                                .sessionManagement(
                                                sessionManagement -> sessionManagement.sessionCreationPolicy(
                                                                SessionCreationPolicy.STATELESS));

                // JWT request filter'ını UsernamePasswordAuthenticationFilter'dan önce ekle
                http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Arrays.asList("*")); // Tüm origins'e izin ver (Flutter uygulaması için)
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
                configuration.setAllowCredentials(false);
                configuration.setMaxAge(3600L);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                // Test için NoOpPasswordEncoder yerine güvenli BCryptPasswordEncoder kullan
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }
}
