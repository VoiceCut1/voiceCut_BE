package hackathon.voice_cut_1.voice_cut_1.controller;

import hackathon.voice_cut_1.voice_cut_1.response.VoicePhishingAnalysisResponse;
import hackathon.voice_cut_1.voice_cut_1.service.VoicePhishingAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/voice-phishing/analysis")
@RequiredArgsConstructor
public class VoicePhishingAnalysisController {

    private final VoicePhishingAnalysisService voicePhishingAnalysisService;

    @PostMapping
    public ResponseEntity<VoicePhishingAnalysisResponse> analysisVoicePhishing(
            @RequestParam("voiceFile") MultipartFile voiceFile
    ) {
        String text = voicePhishingAnalysisService.analysisVoicePhishing("", voiceFile);

        return ResponseEntity.ok(new VoicePhishingAnalysisResponse(text));
    }
}
