package com.example.voicecut1.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void checkScoreAndNotify(Long userId, int score, String fcmToken) {
        if (score > 80) { // 임계값을 80으로 가정
            sendPushNotification(fcmToken, "High Score Alert", "Your score is " + score);
        }
    }

    private void sendPushNotification(String fcmToken, String title, String body) {
        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder().setTitle(title).setBody(body).build())
                .build();

        FirebaseMessaging.getInstance().sendAsync(message);
    }
}