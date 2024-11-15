package hackathon.voice_cut_1.voice_cut_1.controller;

import hackathon.voice_cut_1.voice_cut_1.request.AnalysisVoicePhishingRequest;
import hackathon.voice_cut_1.voice_cut_1.response.VoicePhishingAnalysisResponse;
import hackathon.voice_cut_1.voice_cut_1.service.VoicePhishingAnalysisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/voice-phishing/analysis")
@RequiredArgsConstructor
public class VoicePhishingAnalysisController {

    private final VoicePhishingAnalysisService voicePhishingAnalysisService;

    @PostMapping
    public ResponseEntity<VoicePhishingAnalysisResponse> analysisVoicePhishing(
            @Valid AnalysisVoicePhishingRequest request
    ) {
        String text = voicePhishingAnalysisService.analysisVoicePhishing(request.uuid(), request.voiceFile());

        return ResponseEntity.ok(new VoicePhishingAnalysisResponse(text));
    }
}
