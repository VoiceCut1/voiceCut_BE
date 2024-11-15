package hackathon.voice_cut_1.voice_cut_1.service;

import hackathon.voice_cut_1.voice_cut_1.dto.VoicePhishingAnalysisResultDto;
import hackathon.voice_cut_1.voice_cut_1.entity.Elder;
import hackathon.voice_cut_1.voice_cut_1.exception.ElderNotFoundException;
import hackathon.voice_cut_1.voice_cut_1.feign_client.GptFeignClient;
import hackathon.voice_cut_1.voice_cut_1.feign_client.WhisperFeignClient;
import hackathon.voice_cut_1.voice_cut_1.response.GptFeignClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VoicePhishingAnalysisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final WhisperFeignClient whisperFeignClient;
    private final GptFeignClient gptFeignClient;

    @Value("${openai.key}")
    private String openAiKey;

    @Value("${openai.gpt.model}")
    private String gptModel;

    public VoicePhishingAnalysisResultDto analysisVoicePhishing(
            String uuid,
            MultipartFile voiceFile
    ) {
        Elder elder = (Elder) redisTemplate.opsForValue().get(uuid);

        if (elder == null) {
            throw new ElderNotFoundException();
        }

        String text = convertSpeechToText(voiceFile);

        int percent = analyzeText(text);

        return new VoicePhishingAnalysisResultDto(text, percent);
    }

    // TODO: 외부 API 예외 처리 필요
    private String convertSpeechToText(
            MultipartFile voiceFile
    ) {
        return whisperFeignClient.convertSpeechToText("Bearer " + openAiKey, voiceFile, "whisper-1").text();
    }

    // TODO: 외부 API 예외 처리 필요
    // TODO: 추후 ollama 도입 시 수정
    private int analyzeText(String text) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", gptModel);
        requestBody.put("messages", new Object[]{
                Map.of("role", "system", "content", "You are an assistant that analyzes text for potential voice phishing scams."),
                Map.of("role", "user", "content",
                        "Analyze the following text and return **only the likelihood of voice phishing as a percentage (e.g., 90)**. " +
                                "Do not include any explanation or additional text in your response. " +
                                "Response format must be a single integer percentage:\n" + text)
        });

        GptFeignClientResponse response = gptFeignClient.analyzeText("Bearer " + openAiKey, requestBody);

        return Integer.parseInt(response.choices().get(0).message().content());
    }
}
