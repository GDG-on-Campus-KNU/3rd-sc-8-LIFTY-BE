package gdsc.sc8.LIFTY.controller;

import gdsc.sc8.LIFTY.DTO.ApiResponseDto;
import gdsc.sc8.LIFTY.DTO.auth.LoginRequestDto;
import gdsc.sc8.LIFTY.DTO.auth.SignUpRequestDto;
import gdsc.sc8.LIFTY.DTO.auth.TokenDto;
import gdsc.sc8.LIFTY.DTO.user.UserResponseDto;
import gdsc.sc8.LIFTY.exception.SuccessStatus;
import gdsc.sc8.LIFTY.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ApiResponseDto<UserResponseDto> signup(
        @RequestBody SignUpRequestDto signUpRequestDto) {
        return ApiResponseDto.success(SuccessStatus.SIGNUP_SUCCESS, authService.signup(signUpRequestDto));

    }

    @PostMapping("/login")
    public ApiResponseDto<TokenDto> login(
        @RequestBody LoginRequestDto loginRequestDto) {
        return ApiResponseDto.success(SuccessStatus.LOGIN_SUCCESS, authService.login(loginRequestDto));
    }

}
