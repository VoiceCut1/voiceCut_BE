package hackathon.voice_cut_1.voice_cut_1.entity;

import lombok.*;

import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class Elder {

    private String nickname;
    private Collection<String> guardianNumbers;
}
