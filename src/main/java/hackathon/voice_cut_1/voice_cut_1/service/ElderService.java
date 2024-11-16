package hackathon.voice_cut_1.voice_cut_1.service;

import hackathon.voice_cut_1.voice_cut_1.entity.Elder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ElderService {

    private final RedisTemplate<String, Object> redisTemplate;

    public String saveElderInformationFromRedis(
            String nickname,
            Collection<String> guardianNumbers
    ) {
        String uuid = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set(uuid, new Elder(nickname, guardianNumbers, false), 1, TimeUnit.DAYS);

        return uuid;
    }
}
