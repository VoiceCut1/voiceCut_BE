package com.voicecut.api.llama.service.impl;

import com.voicecut.api.llama.response.LlamaResponse;
import com.voicecut.api.llama.service.ChatClient;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component("customOllamaChatClient")
public class OllamaChatClient implements ChatClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String model;

    public OllamaChatClient(RestTemplate restTemplate,
                            @Value("${spring.ai.ollama.base-url}")
                            String baseUrl,
                            @Value("${spring.ai.ollama.model}")
                            String model
    ) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.model = model;
    }

    @Override
    public LlamaResponse generate(String prompt) {
        String url = String.format("%s/generate", baseUrl);
        Map<String, Object> request = new HashMap<>();
        request.put("model", model);
        request.put("prompt", prompt);
        request.put("stream", false);

        try {
            log.info("Sending request to URL: {}", url);
            log.debug("Request payload: {}", request);

            String response = restTemplate.postForObject(url, request, String.class);
            return formatResponse(response);
        } catch (HttpClientErrorException e) {
            log.error("HTTP error occurred: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }

    private LlamaResponse formatResponse(String response) {
        JSONObject jsonResponse = new JSONObject(response);

        log.info("Raw Response: {}", jsonResponse);
        String model = jsonResponse.optString("model", "unknown model");
        String createdAt = jsonResponse.optString("created_at", "unknown timestamp");
        String message = jsonResponse.optString("response", "No response available");

        message = message.replace("\\n", "\n")
                      .replace("\\\"", "\"")
                      .replace("\\'", "'")
                      .replace("\\\\", "\\"); // 백슬래시 처리

        message = applyMarkdownFormatting(message);

        log.info("Formatted Response:\nModel: {}\nCreated At: {}\nResponse:\n{}", model, createdAt, message);

        return new LlamaResponse(message, createdAt, model);
    }

    private String applyMarkdownFormatting(String text) {

        text = text.replaceAll("\\*\\*(.*?)\\*\\*", "\u001B[1m$1\u001B[0m"); // 굵은 글씨
        text = text.replaceAll("\\*(.*?)\\*", "\u001B[3m$1\u001B[0m");       // 기울임
        return text;
    }
}

