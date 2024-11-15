package hackathon.voice_cut_1.voice_cut_1.service;

import hackathon.voice_cut_1.voice_cut_1.feign_client.WhisperFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoicePhishingAnalysisService {

    private final WhisperFeignClient whisperFeignClient;

    @Value("${openai.key}")
    private String openAiKey;

    public String analysisVoicePhishing(
            String uuid,
            MultipartFile voiceFile
    ) {
        String text = convertSpeechToText(voiceFile);

        log.info("text: {}", text);

        return text;
    }

    private String convertSpeechToText(
            MultipartFile voiceFile
    ) {
        return whisperFeignClient.convertSpeechToText("Bearer " + openAiKey, voiceFile, "whisper-1").text();
    }
}
