package hackathon.voice_cut_1.voice_cut_1.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmService {

    public void sendFcmToSelfAsync(
            String fcmToken,
            String body
    ) {
        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(
                        Notification.builder()
                                .setTitle("[음성감독원] 보이스 피싱 경고")
                                .setBody(body)
                                .build()
                )
                .build();

        // TODO: 예외 처리 추가
        FirebaseMessaging.getInstance().sendAsync(message);
    }
}
