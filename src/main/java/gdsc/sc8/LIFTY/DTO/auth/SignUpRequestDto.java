package gdsc.sc8.LIFTY.DTO.auth;

import gdsc.sc8.LIFTY.domain.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class SignUpRequestDto {

    private String email;

    private String password;

    private String name;

    private String profileUri;

    public User toUser(PasswordEncoder passwordEncoder) {
        return User.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            .name(name)
            .profileUri(profileUri)
            .build();
    }


}
