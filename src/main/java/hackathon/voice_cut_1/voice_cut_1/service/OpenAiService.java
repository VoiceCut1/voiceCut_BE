package hackathon.voice_cut_1.voice_cut_1.service;

import hackathon.voice_cut_1.voice_cut_1.exception.GptFeignClientException;
import hackathon.voice_cut_1.voice_cut_1.exception.WhisperFeignClientException;
import hackathon.voice_cut_1.voice_cut_1.feign_client.GptFeignClient;
import hackathon.voice_cut_1.voice_cut_1.feign_client.WhisperFeignClient;
import hackathon.voice_cut_1.voice_cut_1.response.GptFeignClientResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final WhisperFeignClient whisperFeignClient;
    private final GptFeignClient gptFeignClient;

    @Value("${openai.key}")
    private String openAiKey;

    @Value("${openai.gpt.model}")
    private String openAiGptModel;

    @Async
    public CompletableFuture<String> convertSpeechToTextAsync(
            MultipartFile voiceFile
    ) {
        try {
            String text = whisperFeignClient.convertSpeechToText("Bearer " + openAiKey, voiceFile, "whisper-1").text();

            return CompletableFuture.completedFuture(text);
        } catch (Exception e) {
            log.error("OpenAiService.convertSpeechToTextAsync.Exception: {}", e.getMessage());

            return CompletableFuture.failedFuture(new WhisperFeignClientException());
        }
    }

    // TODO: 추후 ollama 도입 시 수정
    @Async
    public CompletableFuture<Integer> analyzeTextAsync(
            String text
    ) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", openAiGptModel);
        requestBody.put("messages", new Object[]{
                Map.of("role", "system", "content", "You are an assistant that analyzes text for potential voice phishing scams."),
                Map.of("role", "user", "content",
                        "Analyze the following text and return **only the likelihood of voice phishing as a percentage (e.g., 90)**. " +
                                "Do not include any explanation or additional text in your response. " +
                                "Response format must be a single integer percentage:\n" + text)
        });

        GptFeignClientResponse response = gptFeignClient.analyzeText("Bearer " + openAiKey, requestBody);

        try {
            int percent = Integer.parseInt(response.choices().get(0).message().content());
            return CompletableFuture.completedFuture(percent);
        } catch (Exception e) {
            log.error("OpenAiService.analyzeTextAsync.Exception: {}", e.getMessage());

            return CompletableFuture.failedFuture(new GptFeignClientException());
        }
    }
}
