package hackathon.voice_cut_1.voice_cut_1.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GptFeignClientResponse(
        String id,
        String object,
        long created,
        String model,
        Usage usage,
        List<Choice> choices
) {
    public record Usage(
            @JsonProperty("prompt_tokens") int promptTokens,
            @JsonProperty("completion_tokens") int completionTokens,
            @JsonProperty("total_tokens") int totalTokens
    ) {
    }

    public record Choice(
            int index,
            Message message,
            @JsonProperty("finish_reason") String finishReason
    ) {
        public record Message(
                String role,
                String content
        ) {
        }
    }
}
