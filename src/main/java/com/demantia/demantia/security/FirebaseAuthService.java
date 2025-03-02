package com.demantia.demantia.security;

import com.demantia.demantia.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirebaseAuthService {

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * Firebase ile gelen token'ı doğrular ve kendi JWT token'ımızı oluşturur
     */
    public JwtResponse verifyFirebaseTokenAndCreateJwt(String firebaseToken) throws FirebaseAuthException {
        // Firebase ID Token'ını doğrula
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(firebaseToken);

        // Email bilgisini al
        String email = decodedToken.getEmail();

        // Sistemdeki kullanıcıyı kontrol et
        User user = userDetailsService.getUserByEmail(email);

        // Kullanıcı yoksa hata fırlat veya kaydet
        if (user == null) {
            throw new RuntimeException("Bu email ile kayıtlı kullanıcı bulunamadı");
        }

        // UserDetails nesnesi oluştur
        org.springframework.security.core.userdetails.UserDetails userDetails = userDetailsService
                .loadUserByUsername(email);

        // JWT token oluştur
        String token = jwtTokenUtil.generateToken(userDetails, user.getId(), user.getRole());

        // Token yanıtını oluştur
        return new JwtResponse(token, user.getRole(), user.getId());
    }
}
