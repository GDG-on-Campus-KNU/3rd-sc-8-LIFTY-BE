package gdsc.sc8.LIFTY.service;

import gdsc.sc8.LIFTY.DTO.auth.LoginRequestDto;
import gdsc.sc8.LIFTY.DTO.auth.SignUpRequestDto;
import gdsc.sc8.LIFTY.DTO.auth.TokenDto;
import gdsc.sc8.LIFTY.DTO.user.UserResponseDto;
import gdsc.sc8.LIFTY.domain.User;
import gdsc.sc8.LIFTY.exception.ErrorStatus;
import gdsc.sc8.LIFTY.exception.model.CustomException;
import gdsc.sc8.LIFTY.exception.model.NotFoundException;
import gdsc.sc8.LIFTY.infrastructure.UserRepository;
import gdsc.sc8.LIFTY.jwt.TokenProvider;
import jakarta.transaction.Transactional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public UserResponseDto signup(SignUpRequestDto signUpRequestDto) {
        if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new NotFoundException(ErrorStatus.ALREADY_EXIST_EMAIL_EXCEPTION,
                ErrorStatus.ALREADY_EXIST_EMAIL_EXCEPTION.getMessage());
        }

        User user = userRepository.save(signUpRequestDto.toUser(passwordEncoder));
        return UserResponseDto.convertToDto(userRepository.save(user));
    }

    @Transactional
    public TokenDto login(LoginRequestDto loginRequestDto) {
        try {
            // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
            UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();

            // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
            //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
            Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

            // 4. RefreshToken 저장
            redisTemplate.opsForValue().set("RT:" + authentication.getName(),
                tokenDto.getRefreshToken(),
                tokenProvider.getRefreshTokenExpireTime(),
                TimeUnit.MILLISECONDS);

            return tokenDto;
        } catch (AuthenticationException e) {
            throw new NotFoundException(ErrorStatus.WRONG_LOGIN_INFO_EXCEPTION,
                ErrorStatus.WRONG_LOGIN_INFO_EXCEPTION.getMessage());
        } catch (Exception e) {
            throw new CustomException(ErrorStatus.INTERNAL_SERVER_ERROR,
                ErrorStatus.INTERNAL_SERVER_ERROR.getMessage());
        }
    }

    @Transactional
    public TokenDto reissue(String refreshToken) {

        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new NotFoundException(ErrorStatus.EXPIRED_TOKEN,
                ErrorStatus.EXPIRED_TOKEN.getMessage());
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);
        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        String refreshTokenValue = redisTemplate.opsForValue()
            .get("RT:" + authentication.getName());
        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.equals(refreshTokenValue)) {
            throw new NotFoundException(ErrorStatus.INVALID_TOKEN,
                ErrorStatus.INVALID_TOKEN.getMessage());
        }

        // 5. 새로운 토큰 생성
        TokenDto newTokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 Refresh Token 갱신
        redisTemplate.opsForValue().set("RT:" + authentication.getName(),
            newTokenDto.getRefreshToken(),
            tokenProvider.getRefreshTokenExpireTime(),
            TimeUnit.MILLISECONDS);

        // 토큰 발급
        return newTokenDto;
    }
}
