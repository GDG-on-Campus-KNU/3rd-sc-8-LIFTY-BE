package gdsc.sc8.LIFTY.service;

import gdsc.sc8.LIFTY.DTO.user.UserInfoResponseDto;
import gdsc.sc8.LIFTY.domain.User;
import gdsc.sc8.LIFTY.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserInfoResponseDto getUserInfo(String email) {
        log.info("getUserInfo - email: {}", email);
        User user = userRepository.getUserByEmail(email);

        return UserInfoResponseDto.builder()
            .name(user.getName())
            .profileUri(user.getProfileUri())
            .level(user.getLevel())
            .exp(user.getExp())
            .build();
    }

}
