package com.northcoders.jvevents.config.security;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseAuthConfig {

    @PostConstruct
    public void initialize() {
        try {
            String serviceAccountJson = System.getenv("FIREBASE_SERVICE_ACCOUNT_JSON");

            if (serviceAccountJson == null || serviceAccountJson.isEmpty()) {
                throw new RuntimeException("❌ Missing FIREBASE_SERVICE_ACCOUNT_JSON env variable");
            }

            ByteArrayInputStream serviceAccountStream =
                    new ByteArrayInputStream(serviceAccountJson.getBytes(StandardCharsets.UTF_8));

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream)) // <-- checked exception here
                    .setProjectId("jv-events")
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase Admin initialized from env variable.");
            }
        } catch (IOException e) {
            System.err.println("❌ Firebase Admin IO error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Firebase Admin general error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}