package hackathon.voice_cut_1.voice_cut_1.exception;

public class ElderNotFoundException extends BaseException {

    public ElderNotFoundException() {
        super(ErrorCode.ELDER_NOT_FOUND.getMessage(), ErrorCode.ELDER_NOT_FOUND.getHttpStatus());
    }
}
