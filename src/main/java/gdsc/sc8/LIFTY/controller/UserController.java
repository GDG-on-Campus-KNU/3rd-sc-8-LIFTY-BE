package gdsc.sc8.LIFTY.controller;

import gdsc.sc8.LIFTY.DTO.ApiResponseDto;
import gdsc.sc8.LIFTY.DTO.user.UserInfoResponseDto;
import gdsc.sc8.LIFTY.exception.SuccessStatus;
import gdsc.sc8.LIFTY.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "유저 관련 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저의 이름, 프로필 사진, 경험치를 조회합니다.")
    @GetMapping
    public ApiResponseDto<UserInfoResponseDto> getUserInfo(@Parameter(hidden = true) @AuthenticationPrincipal User user) {
        return ApiResponseDto.success(SuccessStatus.GET_USER_INFO_SUCCESS, userService.getUserInfo(user.getUsername()));
    }

}
