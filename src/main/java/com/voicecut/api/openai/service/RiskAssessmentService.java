package com.voicecut.api.openai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.voicecut.api.openai.dto.RiskAssessmentRequest;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Slf4j
@Service
public class RiskAssessmentService {

    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private final String model;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    @Autowired
    public RiskAssessmentService(RestTemplate restTemplate, HttpHeaders headers,
                                 @Value("${openai.model}") String model) {
        this.restTemplate = restTemplate;
        this.headers = headers;
        this.model = model;
    }

    public ResponseEntity<String> assessRisk(RiskAssessmentRequest request) throws JsonProcessingException {
        // TODO: 사용자 별 id 값 추가
        String prompt = generatePrompt(request.text());

        // TODO: 사용자 대화기록 생성
        JSONObject messageSystem = new JSONObject();
        messageSystem.put("role", "system"); // 역할 설정
        messageSystem.put("content", prompt); // 프롬프트 설정

        JSONObject messageUser = new JSONObject();
        messageUser.put("role", "user"); // 역할 설정
        messageUser.put("content", request.text()); // 사용자 입력 설정

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", model); // 모델 설정
        requestBody.put("messages", new JSONArray(Arrays.asList(messageSystem, messageUser))); // 메시지 설정

        HttpEntity<String> gptRequest = new HttpEntity<>(requestBody.toString(), headers);

        try {
            ResponseEntity<String> gptResponse = restTemplate.postForEntity(OPENAI_API_URL, gptRequest, String.class);
            return gptResponse.getStatusCode() == HttpStatus.OK && gptResponse.getBody() != null
                       ? gptResponse
                       : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("API 호출 중 오류 발생!");
        } catch (Exception e) {
            log.error("API 호출 중 예외 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Exception occurred: " + e.getMessage());
        }
    }

    // TODO: 대화 기록 초기화 추가 ( 통화 종료 시)

    private String generatePrompt(String callTranscript) {
        return String.format("다음은 통화 내역입니다.\n\n%s\n\n통화 내역을 분석하여 보이스피싱 위험도를 0부터 100까지의 정수로 평가해 주세요. 결과는 숫자만 응답해 주세요.", callTranscript);
    }
}
