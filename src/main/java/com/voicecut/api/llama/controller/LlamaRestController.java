package com.voicecut.api.llama.controller;

import com.voicecut.api.llama.response.LlamaResponse;
import com.voicecut.api.llama.service.impl.LlamaAiServiceImpl;
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
        @RequestParam(value = "promptMessage", defaultValue = "Why is the sky blue?")
        String promptMessage) {
        final LlamaResponse aiResponse = llamaAiService.generateMessage(promptMessage);
        return ResponseEntity.status(HttpStatus.OK).body(aiResponse);
    }

    @PostMapping("api/v1/ai/generate/joke/{topic}")
    public ResponseEntity<LlamaResponse> generateJoke(@PathVariable String topic) {
        final LlamaResponse aiResponse = llamaAiService.generateJoke(topic);
        return ResponseEntity.status(HttpStatus.OK).body(aiResponse);
    }
}