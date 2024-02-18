package gdsc.sc8.LIFTY.oauth2;

import gdsc.sc8.LIFTY.domain.User;
import gdsc.sc8.LIFTY.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReissueTokenService {
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final UserRepository userRepository;

    public String reissueToken(String clientRegistrationId, String principalName){

        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        clientRegistrationId,
                        principalName);
        OAuth2RefreshToken refreshToken = client.getRefreshToken();
        OAuth2AccessToken accessToken = client.getAccessToken();
        if (accessToken.getExpiresAt().isBefore(Instant.now())) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        log.info(String.valueOf(refreshToken));
        log.info(accessToken.getTokenValue());

        User user = userRepository.getUserByEmail(principalName);
        user.setGeminiToken(accessToken.getTokenValue());
        userRepository.save(user);

        return accessToken.getTokenValue();
    }
}
