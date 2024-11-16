package hackathon.voice_cut_1.voice_cut_1.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Notification;
import hackathon.voice_cut_1.voice_cut_1.entity.Elder;
import hackathon.voice_cut_1.voice_cut_1.exception.ElderNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoicePhishingAnalysisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final OpenAiService openAiService;
    private final SmsService smsService;

    public void analysisVoicePhishing(
            String uuid,
            MultipartFile voiceFile
    ) {
        Elder elder = (Elder) redisTemplate.opsForValue().get(uuid);

        if (elder == null) {
            throw new ElderNotFoundException();
        }

        // TODO: 예외 처리 추가
        openAiService.convertSpeechToTextAsync(voiceFile)
                .thenCompose(text -> openAiService.analyzeTextAsync(text)
                        .thenApply(percent -> Map.of("text", text, "percent", percent)))
                .thenAccept(result -> {
                    String text = (String) result.get("text");
                    int percent = (Integer) result.get("percent");

                    log.info("text: {}, percent: {}", text, percent);

                    //Fcm 기능 추가
                    if (percent >= 80 && percent < 90  && !elder.isSendMessageAt80Percent()){
                        sendAlarmToSelf(elder.getFcmToken(), "경고 : 현재 통화는 보이스 피싱일 가능성이 높습니다!");

                        redisTemplate.opsForValue().set(uuid, new Elder(elder.getNickname(), elder.getGuardianNumbers(), true,false
                                , elder.getFcmToken()));

                    } else if (percent >= 90 && !elder.isSendMessageAt90Percent()) {
                        sendAlarmToSelf(elder.getFcmToken(), "경고 : 현재 통화는 보이스 피싱일 가능성이 아주 높습니다!");

                        // TODO: 예외 처리 추가
                        smsService.sendSmsToGuardianNumbersAsync(elder.getNickname(), elder.getGuardianNumbers())
                                .thenAccept(ignored -> redisTemplate.opsForValue().set(uuid, new Elder(elder.getNickname(), elder.getGuardianNumbers(), true)));
                    }
                });
    }

    private void sendAlarmToSelf(
            String fcmToken,
            String body) {
        com.google.firebase.messaging.Message message = com.google.firebase.messaging.Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder().setTitle("[음성감독원] 보이스 피싱 경고").setBody(body).build())
                .build();

        FirebaseMessaging.getInstance().sendAsync(message);
    }
}
