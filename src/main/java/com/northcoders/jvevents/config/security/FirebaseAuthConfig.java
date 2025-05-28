package com.northcoders.jvevents.config.security;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.InputStream;

@Configuration
public class FirebaseAuthConfig {

    @PostConstruct
    public void initialize() {
        try {
            InputStream serviceAccount = FirebaseAuthConfig.class
                    .getClassLoader()
                    .getResourceAsStream("serviceAccountKey.json");

            if (serviceAccount == null) {
                throw new RuntimeException("❌ Could not find serviceAccountKey.json in classpath");
            }
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
