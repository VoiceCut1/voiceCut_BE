package hackathon.voice_cut_1.voice_cut_1.request;

import java.util.Collection;

public record SaveElderInformationRequest(
        String nickname,
        Collection<String> guardianNumbers
) {
}
