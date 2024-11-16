package hackathon.voice_cut_1.voice_cut_1.feign_client;

import hackathon.voice_cut_1.voice_cut_1.response.WhisperFeignClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(
        name = "WhisperFeignClient",
        url = "${openai.whisper.url}"
)
public interface WhisperFeignClient {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    WhisperFeignClientResponse convertSpeechToText(
            @RequestHeader("Authorization") String authorization,
            @RequestPart("file") MultipartFile voiceFile,
            @RequestPart("model") String model
    );
}
