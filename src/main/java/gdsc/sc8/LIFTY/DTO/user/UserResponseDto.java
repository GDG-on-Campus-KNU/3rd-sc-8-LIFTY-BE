package gdsc.sc8.LIFTY.DTO.user;

import gdsc.sc8.LIFTY.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {

    private String email;

    private String name;

    private String profileUri;

    public static UserResponseDto convertToDto(User user) {
        return UserResponseDto.builder()
            .email(user.getEmail())
            .name(user.getName())
            .profileUri(user.getProfileUri())
            .build();
    }
}
