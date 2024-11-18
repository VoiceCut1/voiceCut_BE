package hackathon.voice_cut_1.voice_cut_1.exception;

public class GptFeignClientException extends BaseException {

    public GptFeignClientException() {
        super(ErrorCode.GPT_FEIGN_CLIENT_FAILED.getMessage(), ErrorCode.GPT_FEIGN_CLIENT_FAILED.getHttpStatus());
    }
}
