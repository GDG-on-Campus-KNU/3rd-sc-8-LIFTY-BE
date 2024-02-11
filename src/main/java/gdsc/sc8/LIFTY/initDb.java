package gdsc.sc8.LIFTY;

import gdsc.sc8.LIFTY.domain.User;
import gdsc.sc8.LIFTY.infrastructure.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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

        public void dbInit() {
            User user = User.builder()
                .email("test")
                .name("홍길동")
                .password(passwordEncoder.encode("test"))
                .profileUri("https://google.com")
                .build();
            userRepository.save(user);
        }
    }

}
