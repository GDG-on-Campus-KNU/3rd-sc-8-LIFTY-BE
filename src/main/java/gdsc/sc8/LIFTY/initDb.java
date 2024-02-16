package gdsc.sc8.LIFTY;

import gdsc.sc8.LIFTY.domain.Diary;
import gdsc.sc8.LIFTY.domain.Emotion;
import gdsc.sc8.LIFTY.domain.User;
import gdsc.sc8.LIFTY.infrastructure.DiaryRepository;
import gdsc.sc8.LIFTY.infrastructure.EmotionRepository;
import gdsc.sc8.LIFTY.infrastructure.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class initDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final DiaryRepository diaryRepository;
        private final EmotionRepository emotionRepository;



        public void dbInit() {
            User user = User.builder()
                .email("test")
                .name("홍길동")
                .password(passwordEncoder.encode("test"))
                .profileUri("https://google.com")
                .build();
            userRepository.save(user);


            for(int i=-2; i<2; i++){
                Diary diary = new Diary(user,LocalDate.now().plusDays(i),
                        "오늘은 하루 종일 비가 내렸다. 창밖으로 보이는 풍경은 우중충하고 우울했다. 하지만 비 소리를 들으며 차분한 시간을 보낼 수 있어 기분이 좋았다.");

                diaryRepository.save(diary);
                emotionRepository.save(new Emotion(diary,"행복"));
                emotionRepository.save(new Emotion(diary,"우울"));

            }

        }
    }

}
