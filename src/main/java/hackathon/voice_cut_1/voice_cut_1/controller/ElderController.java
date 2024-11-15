package hackathon.voice_cut_1.voice_cut_1.controller;

import hackathon.voice_cut_1.voice_cut_1.request.SaveElderInformationRequest;
import hackathon.voice_cut_1.voice_cut_1.response.SaveElderInformationResponse;
import hackathon.voice_cut_1.voice_cut_1.service.ElderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/elders")
@RequiredArgsConstructor
public class ElderController {

    private final ElderService elderService;

    @PostMapping
    public ResponseEntity<SaveElderInformationResponse> saveElderInformation(
            @Valid @RequestBody SaveElderInformationRequest request
    ) {
        String uuid = elderService.saveElderInformationFromRedis(request.nickname(), request.guardianNumbers());

        return ResponseEntity.ok(new SaveElderInformationResponse(uuid));
    }
}
