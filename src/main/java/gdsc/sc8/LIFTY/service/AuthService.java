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
            e.printStackTrace();
            throw new NotFoundException(ErrorStatus.WRONG_LOGIN_INFO_EXCEPTION,
                ErrorStatus.WRONG_LOGIN_INFO_EXCEPTION.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorStatus.INTERNAL_SERVER_ERROR,
                ErrorStatus.INTERNAL_SERVER_ERROR.getMessage());
        }
    }
}
