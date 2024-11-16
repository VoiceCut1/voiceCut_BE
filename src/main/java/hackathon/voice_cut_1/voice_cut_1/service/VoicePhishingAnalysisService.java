package hackathon.voice_cut_1.voice_cut_1.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final DiscordNotificationService discordNotificationService;

    private final ObjectMapper objectMapper;

    public void analysisVoicePhishing(
            String uuid,
            MultipartFile voiceFile
    ) {
        Elder elder = (Elder) redisTemplate.opsForValue().get(uuid);

        if (elder == null) {
            throw new ElderNotFoundException();
        }

        openAiService.convertSpeechToTextAsync(voiceFile)
                .thenCompose(text -> openAiService.analyzeTextAsync(text)
                        .thenApply(percent -> Map.of("text", text, "percent", percent)))
                .thenAccept(result -> {
                    String text = (String) result.get("text");
                    int percent = (Integer) result.get("percent");

                    log.info("text: {}, percent: {}", text, percent);

                    if (percent >= 80 && percent < 90 && !elder.isSendMessageAt80Percent()) {
                        fcmService.sendFcmToSelfAsync(elder.getFcmToken(), "경고 : 현재 통화는 보이스 피싱일 가능성이 높습니다!")
                                .thenAccept(ignored -> redisTemplate.opsForValue().set(uuid, new Elder(elder.getNickname(), elder.getFcmToken(), elder.getGuardianNumbers(), true, false)))
                                .exceptionally(throwable -> {
                                    discordNotificationService.sendExceptionMessageAsync(getFcmExceptionMessage(throwable));
                                    return null;
                                });
                    } else if (percent >= 90 && !elder.isSendMessageAt90Percent()) {
                        // Q-noah. SMS 이후 FCM이 처리되는 경우, 90% 변수에 문제가 없는가?

                        fcmService.sendFcmToSelfAsync(elder.getFcmToken(), "경고 : 현재 통화는 보이스 피싱일 가능성이 아주 높습니다!")
                                .thenAccept(ignored -> redisTemplate.opsForValue().set(uuid, new Elder(elder.getNickname(), elder.getFcmToken(), elder.getGuardianNumbers(), true, false)))
                                .exceptionally(throwable -> {
                                    discordNotificationService.sendExceptionMessageAsync(getFcmExceptionMessage(throwable));
                                    return null;
                                });

                        smsService.sendSmsToGuardianNumbersAsync(elder.getNickname(), elder.getGuardianNumbers())
                                .thenAccept(ignored -> redisTemplate.opsForValue().set(uuid, new Elder(elder.getNickname(), elder.getFcmToken(), elder.getGuardianNumbers(), true, true)))
                                .exceptionally(throwable -> {
                                    discordNotificationService.sendExceptionMessageAsync(getSmsExceptionMessage(throwable));
                                    return null;
                                });
                    }
                })
                .exceptionally(throwable -> {
                    discordNotificationService.sendExceptionMessageAsync(getOpenAiExceptionMessage(throwable));
                    return null;
                });
    }

    private String getOpenAiExceptionMessage(Throwable throwable) {
        try {
            String message = throwable.getMessage();

            return "OpenAI Exception: " + objectMapper
                    .readTree(message.substring(message.indexOf("{"), message.lastIndexOf("}") + 1))
                    .at("/error/message")
                    .asText();
        } catch (JsonProcessingException exception) {
            return "OpenAI Exception: OPENAI_EXCEPTION_MESSAGE_PARSING_FAILED";
        }
    }

    private String getFcmExceptionMessage(Throwable throwable) {
        String message = throwable.getMessage();

        String[] strings = message.split(": ");

        return "FCM Exception: " + strings[1];
    }

    private String getSmsExceptionMessage(Throwable throwable) {
        String message = throwable.getMessage();

        String[] strings = message.split(": ");

        return "SMS Exception: " + strings[1];
    }
}
