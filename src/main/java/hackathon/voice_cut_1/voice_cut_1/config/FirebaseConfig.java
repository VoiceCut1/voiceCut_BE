package hackathon.voice_cut_1.voice_cut_1.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
public class FirebaseConfig {
    @PostConstruct
    public void initializeFirebase() {
        try (InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase/serviceAccountKey.json")) {
            if (serviceAccount == null) {
                throw new RuntimeException("Firebase service account file not found");
            }

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize Firebase", e);
        }
    }
}
