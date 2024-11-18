package hackathon.voice_cut_1.voice_cut_1.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import hackathon.voice_cut_1.voice_cut_1.exception.FcmSendFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class FcmService {

    public CompletableFuture<Void> sendFcmToSelfAsync(
            String fcmToken,
            String title,
            String body
    ) {
        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(
                        Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build()
                )
                .build();

        return CompletableFuture.supplyAsync(() -> {
            try {
                FirebaseMessaging.getInstance().send(message);

                return null;
            } catch (FirebaseMessagingException e) {
                throw new FcmSendFailedException();
            }
        });
    }
}
