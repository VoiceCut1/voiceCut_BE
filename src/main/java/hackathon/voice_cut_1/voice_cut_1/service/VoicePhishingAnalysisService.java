package hackathon.voice_cut_1.voice_cut_1.service;

import hackathon.voice_cut_1.voice_cut_1.entity.Elder;
import hackathon.voice_cut_1.voice_cut_1.exception.ElderNotFoundException;
import hackathon.voice_cut_1.voice_cut_1.exception.SmsSendFailedException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${coolsms.key}")
    private String coolsmsKey;

    @Value("${coolsms.secret-key}")
    private String coolsmsSecretKey;

    @Value("${coolsms.calling-number}")
    private String coolsmsCallingNumber;

    @Value("${coolsms.domain}")
    private String coolsmsDomain;

    private DefaultMessageService messageService;

    @PostConstruct
    public void init() {
        messageService = NurigoApp.INSTANCE.initialize(coolsmsKey, coolsmsSecretKey, coolsmsDomain);
    }

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

                    // TODO: FCM 로직 추기

                    if (percent >= 90 && !elder.isSendMessageAt90Percent()) {
                        sendSmsToGuardianNumbers(elder.getNickname(), elder.getGuardianNumbers());

                        redisTemplate.opsForValue().set(uuid, new Elder(elder.getNickname(), elder.getGuardianNumbers(), true));
                    }
                });
    }

    private void sendSmsToGuardianNumbers(
            String nickname,
            Collection<String> guardianNumbers
    ) {
        List<Message> messages = new ArrayList<>();

        for (String guardianNumber : guardianNumbers) {
            Message message = new Message();
            message.setFrom(coolsmsCallingNumber);
            message.setTo(guardianNumber);
            message.setText("[음성감독원] " + nickname + " 어르신이 보이스 피싱을 당하고 있습니다!");

            messages.add(message);
        }

        try {
            messageService.send(messages);
        } catch (NurigoMessageNotReceivedException | NurigoEmptyResponseException | NurigoUnknownException exception) {
            throw new SmsSendFailedException();
        }
    }
}
