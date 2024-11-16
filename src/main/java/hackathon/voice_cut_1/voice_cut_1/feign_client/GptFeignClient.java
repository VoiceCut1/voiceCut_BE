package hackathon.voice_cut_1.voice_cut_1.feign_client;

import hackathon.voice_cut_1.voice_cut_1.response.GptFeignClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(
        name = "GptFeignClient",
        url = "${openai.gpt.url}")
public interface GptFeignClient {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    GptFeignClientResponse analyzeText(
            @RequestHeader("Authorization") String authorization,
            @RequestBody Map<String, Object> requestBody
    );
}
