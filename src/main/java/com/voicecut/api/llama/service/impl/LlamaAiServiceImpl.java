package com.voicecut.api.llama.service.impl;

import com.voicecut.api.llama.response.LlamaResponse;
import com.voicecut.api.llama.service.LlamaAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LlamaAiServiceImpl implements LlamaAiService {

    @Autowired
    private  OllamaChatClient chatClient;

    @Override
    public LlamaResponse generateMessage(String promptMessage) {
        return chatClient.generate(promptMessage);
    }

    @Override
    public LlamaResponse generateVoiceCut(String topic) {
        return chatClient.generate(String.format("Tell me a joke about %s", topic));
    }
}