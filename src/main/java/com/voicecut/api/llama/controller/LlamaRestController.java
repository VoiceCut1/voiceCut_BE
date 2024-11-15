package com.voicecut.api.llama.controller;

import com.voicecut.api.llama.response.LlamaResponse;
import com.voicecut.api.llama.service.impl.LlamaAiServiceImpl;
import com.voicecut.api.llama.service.impl.OllamaChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LlamaRestController {

    @Autowired
    private LlamaAiServiceImpl llamaAiService;

    @GetMapping("api/v1/ai/generate")
    public ResponseEntity<LlamaResponse> generate(
        @RequestParam(value = "promptMessage", defaultValue = "한국말로 대답해 이제부터, 안녕!")
        String promptMessage) {
        final LlamaResponse aiResponse = llamaAiService.generateMessage(promptMessage);
        return ResponseEntity.status(HttpStatus.OK).body(aiResponse);
    }

    @PostMapping("api/v1/ai/generate/voicecut")
    public ResponseEntity<LlamaResponse> generateJoke(@RequestBody String message) {
        final LlamaResponse aiResponse = llamaAiService.generateVoiceCut("Calculate the voice phishing risk" + message);
        return ResponseEntity.status(HttpStatus.OK).body(aiResponse);
    }
}