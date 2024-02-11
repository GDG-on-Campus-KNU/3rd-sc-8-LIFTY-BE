package gdsc.sc8.LIFTY.oauth2;

import gdsc.sc8.LIFTY.domain.User;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {

    private final String nameAttributeKey;
    private final OAuth2UserInfo oAuth2UserInfo;

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName,
        Map<String, Object> attributes) {
        return OAuthAttributes.builder()
            .nameAttributeKey(userNameAttributeName)
            .oAuth2UserInfo(new GoogleOAuth2UserInfo(attributes))
            .build();
    }

    public User toEntity(OAuth2UserInfo oAuth2UserInfo) {
        return User.builder()
            .name(oAuth2UserInfo.getName())
            .email(oAuth2UserInfo.getEmail())
            .profileUri(oAuth2UserInfo.getPicture())
            .build();
    }

}
