package com.demantia.demantia.security;

import com.demantia.demantia.model.User;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            System.out.println("Loading user by email: " + email);

            Firestore firestore = FirestoreClient.getFirestore();

            // Kullanıcı e-posta adresine göre sorgulanıyor
            QuerySnapshot querySnapshot = firestore.collection("users")
                    .whereEqualTo("email", email)
                    .get().get();

            if (!querySnapshot.isEmpty()) {
                QueryDocumentSnapshot document = querySnapshot.getDocuments().get(0);
                String role = (String) document.get("role");

                // Null kontrolü ekle
                if (role == null) {
                    role = "USER"; // Varsayılan rol
                }

                System.out.println("User found with role: " + role);

                // Spring Security için kullanıcıyı oluştur
                return new org.springframework.security.core.userdetails.User(
                        email,
                        "{noop}password", // Firebase Auth kullandığımız için placeholder
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())));
            } else {
                System.out.println("User not found with email: " + email);
                throw new UsernameNotFoundException("User not found with email: " + email);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error loading user: " + e.getMessage());
            e.printStackTrace();
            throw new UsernameNotFoundException("Error loading user", e);
        }
    }

    public User getUserByEmail(String email) {
        try {
            Firestore firestore = FirestoreClient.getFirestore();
            QuerySnapshot querySnapshot = firestore.collection("users")
                    .whereEqualTo("email", email)
                    .get().get();

            if (!querySnapshot.isEmpty()) {
                QueryDocumentSnapshot document = querySnapshot.getDocuments().get(0);

                User user = new User();
                user.setId(document.getId());
                user.setEmail(email);
                user.setName((String) document.get("name"));
                user.setRole((String) document.get("role"));

                return user;
            }
            return null;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Firebase'den gelen kullanıcı bilgilerini Firestore'a kaydeder veya günceller
     */
    public User createOrUpdateFirebaseUser(String email, String name, String firebaseUid) {
        try {
            User existingUser = getUserByEmail(email);

            if (existingUser != null) {
                return existingUser; // Kullanıcı zaten var
            }

            // Yeni kullanıcı oluştur
            Firestore firestore = FirestoreClient.getFirestore();

            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setRole("USER"); // Varsayılan rol
            newUser.setFirebaseUid(firebaseUid);

            // Firestore'a kaydet
            firestore.collection("users").document().set(newUser);

            // Oluşturulan kullanıcıyı döndür
            return getUserByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
