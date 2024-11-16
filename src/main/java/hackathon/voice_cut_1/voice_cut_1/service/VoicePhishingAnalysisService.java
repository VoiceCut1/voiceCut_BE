package hackathon.voice_cut_1.voice_cut_1.service;

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
    private final FcmService fcmService;
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

                    if (percent >= 80 && percent < 90 && !elder.isSendMessageAt80Percent()) {
                        fcmService.sendFcmToSelfAsync(elder.getFcmToken(), "경고 : 현재 통화는 보이스 피싱일 가능성이 높습니다!");

                        redisTemplate.opsForValue().set(uuid, new Elder(elder.getNickname(), elder.getFcmToken(), elder.getGuardianNumbers(), true, false));

                    } else if (percent >= 90 && !elder.isSendMessageAt90Percent()) {
                        fcmService.sendFcmToSelfAsync(elder.getFcmToken(), "경고 : 현재 통화는 보이스 피싱일 가능성이 아주 높습니다!");

                        // TODO: 예외 처리 추가
                        smsService.sendSmsToGuardianNumbersAsync(elder.getNickname(), elder.getGuardianNumbers())
                                .thenAccept(ignored -> redisTemplate.opsForValue().set(uuid, new Elder(elder.getNickname(), elder.getFcmToken(), elder.getGuardianNumbers(), true, true)));
                    }
                });
    }
}
