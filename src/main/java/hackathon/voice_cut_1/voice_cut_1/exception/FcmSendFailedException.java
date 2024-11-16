package hackathon.voice_cut_1.voice_cut_1.exception;

public class FcmSendFailedException extends BaseException {

    public FcmSendFailedException() {
        super(ErrorCode.FCM_SEND_FAILED.getMessage(), ErrorCode.FCM_SEND_FAILED.getHttpStatus());
    }
}