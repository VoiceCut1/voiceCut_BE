package hackathon.voice_cut_1.voice_cut_1.exception;

public class WhisperFeignClientException extends BaseException {

    public WhisperFeignClientException() {
        super(ErrorCode.WHISPER_FEIGN_CLIENT_FAILED.getMessage(), ErrorCode.WHISPER_FEIGN_CLIENT_FAILED.getHttpStatus());
    }
}