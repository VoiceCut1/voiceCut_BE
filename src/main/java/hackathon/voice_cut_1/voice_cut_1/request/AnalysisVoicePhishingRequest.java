package hackathon.voice_cut_1.voice_cut_1.request;

import hackathon.voice_cut_1.voice_cut_1.annotation.VoiceFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.multipart.MultipartFile;

public record AnalysisVoicePhishingRequest(
        @NotBlank @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$") String uuid,
        @VoiceFile MultipartFile voiceFile
) {
}
