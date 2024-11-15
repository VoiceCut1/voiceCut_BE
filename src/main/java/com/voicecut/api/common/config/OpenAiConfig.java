package com.voicecut.api.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;


@Configuration
public class OpenAiConfig {

    private final String openAiKey;
    private final String model;

    public OpenAiConfig(
        @Value("${openai.api.key}") String openAiKey,
        @Value("${openai.model}") String model
    ) {
        this.openAiKey = openAiKey;
        this.model = model;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public HttpHeaders httpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(openAiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
