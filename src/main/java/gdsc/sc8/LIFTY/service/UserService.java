package gdsc.sc8.LIFTY.service;

import gdsc.sc8.LIFTY.DTO.user.UserInfoResponseDto;
import gdsc.sc8.LIFTY.domain.User;
import gdsc.sc8.LIFTY.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserInfoResponseDto getUserInfo(String username) {
        User user = userRepository.getUserByEmail(username);

        return UserInfoResponseDto.builder()
            .name(user.getName())
            .profileUri(user.getProfileUri())

            .build();
    }

}
