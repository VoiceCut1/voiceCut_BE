package hackathon.voice_cut_1.voice_cut_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class VoiceCut1Application {

    public static void main(String[] args) {
        SpringApplication.run(VoiceCut1Application.class, args);
    }
}
