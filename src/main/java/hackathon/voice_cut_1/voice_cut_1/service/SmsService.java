package hackathon.voice_cut_1.voice_cut_1.service;

import hackathon.voice_cut_1.voice_cut_1.exception.SmsSendFailedException;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final DefaultMessageService messageService;

    @Value("${coolsms.calling-number}")
    private String coolsmsCallingNumber;

    @Async
    public CompletableFuture<Void> sendSmsToGuardianNumbersAsync(
            String nickname,
            Collection<String> guardianNumbers
    ) {
        List<Message> messages = new ArrayList<>();

        for (String guardianNumber : guardianNumbers) {
            Message message = new Message();
            message.setFrom(coolsmsCallingNumber);
            message.setTo(guardianNumber);
            message.setText("[음성감독원] " + nickname + " 어르신이 보이스 피싱을 당하고 있습니다!");

            messages.add(message);
        }

        return CompletableFuture.runAsync(() -> {
            try {
                messageService.send(messages);
            } catch (NurigoMessageNotReceivedException | NurigoEmptyResponseException | NurigoUnknownException exception) {
                throw new SmsSendFailedException();
            }
        });
    }
}
