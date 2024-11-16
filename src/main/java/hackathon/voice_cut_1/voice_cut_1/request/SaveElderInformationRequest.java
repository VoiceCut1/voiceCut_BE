package hackathon.voice_cut_1.voice_cut_1.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.Collection;

public record SaveElderInformationRequest(
        @NotBlank String nickname,
        @NotEmpty Collection<@NotBlank @Pattern(regexp = "\\d+") String> guardianNumbers
) {
}
