package com.voicecut.api.llama.service;

import com.voicecut.api.llama.response.LlamaResponse;

public interface ChatClient {
    LlamaResponse generate(String prompt);
}
