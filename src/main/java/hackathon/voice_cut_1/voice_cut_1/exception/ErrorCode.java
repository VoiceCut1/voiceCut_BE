package hackathon.voice_cut_1.voice_cut_1.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    ELDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ELDER_NOT_FOUND");

    private final HttpStatus httpStatus;
    private final String message;
}
