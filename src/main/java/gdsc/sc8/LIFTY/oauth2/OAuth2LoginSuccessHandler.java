package gdsc.sc8.LIFTY.oauth2;

import gdsc.sc8.LIFTY.DTO.auth.TokenDto;
import gdsc.sc8.LIFTY.domain.User;
import gdsc.sc8.LIFTY.enums.Authority;
import gdsc.sc8.LIFTY.infrastructure.UserRepository;
import gdsc.sc8.LIFTY.jwt.TokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final ReissueTokenService reissueTokenService;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String id = customOAuth2User.getSocialId();

        reissueTokenService.reissueToken("google",customOAuth2User.getName());

        // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트
        if(customOAuth2User.getAuthority() == Authority.ROLE_GUEST) {
            String accessToken = tokenProvider.generateTokenDto(authentication).getAccessToken();
            response.sendRedirect("http://localhost:3000/socialLoginNameInput"+"?token="+accessToken+"&id="+id); // 프론트의 회원가입 추가 정보 입력 폼으로 리다이렉트 (추가 컨트롤러를 만들고 거기서 post 해야지 User로 바뀜)
            return;
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        redisTemplate.opsForValue().set("RT:" + authentication.getName(),
            tokenDto.getRefreshToken(),
            tokenProvider.getRefreshTokenExpireTime(),
            TimeUnit.MILLISECONDS);

        response.sendRedirect("http://localhost:8002/social-redirecting"+"?accessToken="+tokenDto.getAccessToken()+"&refreshToken="+tokenDto.getRefreshToken());
    }
}
