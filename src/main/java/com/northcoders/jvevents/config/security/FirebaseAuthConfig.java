package com.northcoders.jvevents.config.security;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseAuthConfig {

    @PostConstruct
    public void initialize() {
        try {
            String serviceAccountJson = System.getenv("FIREBASE_SERVICE_ACCOUNT_JSON");
            InputStream serviceAccount = new ByteArrayInputStream(serviceAccountJson.getBytes(StandardCharsets.UTF_8));

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId("jv-events") // Ensure this is the correct project ID
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase Admin SDK initialized.");
            }
        } catch (Exception e) {
            System.err.println("❌ Firebase Admin initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
