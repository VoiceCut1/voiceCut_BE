package hackathon.voice_cut_1.voice_cut_1.config;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsConfig {

    @Value("${coolsms.key}")
    private String coolsmsKey;

    @Value("${coolsms.secret-key}")
    private String coolsmsSecretKey;

    @Value("${coolsms.domain}")
    private String coolsmsDomain;

    @Bean
    public DefaultMessageService defaultMessageService() {
        return NurigoApp.INSTANCE.initialize(coolsmsKey, coolsmsSecretKey, coolsmsDomain);
    }
}
