package hackathon.voice_cut_1.voice_cut_1.exception;

public class SmsSendFailedException extends BaseException {

    public SmsSendFailedException() {
        super(ErrorCode.SMS_SEND_FAILED.getMessage(), ErrorCode.SMS_SEND_FAILED.getHttpStatus());
    }
}
