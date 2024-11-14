package com.voicecut.api.llama.service;

import com.voicecut.api.llama.response.LlamaResponse;

public interface LlamaAiService {
    LlamaResponse generateMessage(String prompt);
    LlamaResponse generateJoke(String topic);
}
