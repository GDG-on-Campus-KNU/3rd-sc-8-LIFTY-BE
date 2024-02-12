package gdsc.sc8.LIFTY.oauth2;

import gdsc.sc8.LIFTY.enums.Authority;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private final Authority authority;
    private final String socialId;

    public CustomOAuth2User(
        Collection<? extends GrantedAuthority> authorities,
        Map<String, Object> attributes, String nameAttributeKey,
        Authority authority, String socialId) {
        super(authorities, attributes, nameAttributeKey);
        this.authority = authority;
        this.socialId = socialId;
    }

    // @AuthenticationPrincipal User user에서 OAuth2 유저와 일반 유저 동일하게 사용하기 위해 필요
    @Override
    public String getName() {
        return (String) this.getAttributes().get("email");
    }
}
