package gdsc.sc8.LIFTY.oauth2;

import gdsc.sc8.LIFTY.domain.User;
import gdsc.sc8.LIFTY.infrastructure.UserRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        System.out.println(userRequest.getAccessToken().getTokenValue());
        User user = getUser(attributes,userRequest.getAccessToken().getTokenValue());

        return new CustomOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(user.getAuthority().name())),
            oAuth2User.getAttributes(),
            attributes.getNameAttributeKey(),
            user.getAuthority(),
            attributes.getOAuth2UserInfo().getId()
        );
    }

    private User getUser(OAuthAttributes attributes,String geminiToken) {
        User user = userRepository.findBySocialId(attributes.getOAuth2UserInfo().getId()).orElse(null);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            return saveUser(attributes, authentication.getName(), geminiToken);
        }

        if (user == null) {
            return saveUser(attributes, geminiToken);
        }
        return user;
    }

    private User saveUser(OAuthAttributes attributes, String email, String geminiToken) {
        User user = userRepository.getUserByEmail(email);
        user.changeSocialId(attributes.getOAuth2UserInfo().getId());
        user.setGeminiToken(geminiToken);
        return userRepository.save(user);
    }

    private User saveUser(OAuthAttributes attributes, String geminiToken) {
        User user = attributes.toEntity(attributes.getOAuth2UserInfo());
        user.setGeminiToken(geminiToken);
        return userRepository.save(user);
    }
}
