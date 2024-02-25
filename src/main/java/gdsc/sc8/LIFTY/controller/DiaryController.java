package gdsc.sc8.LIFTY.controller;

import gdsc.sc8.LIFTY.DTO.ApiResponseDto;
import gdsc.sc8.LIFTY.DTO.diary.DiaryResponseDto;
import gdsc.sc8.LIFTY.exception.SuccessStatus;
import gdsc.sc8.LIFTY.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/diary")
@RequiredArgsConstructor
@SecurityRequirement(name = "Access Token")
@Tag(name = "Diary", description = "일기 관련 API")
public class DiaryController {
    private final DiaryService diaryService;
    @Operation(summary = "사용자의 모든 일기내역을 조회합니다.")
    @GetMapping
    public ApiResponseDto<List<DiaryResponseDto>> getUserInfo(
            @Parameter(hidden = true) @AuthenticationPrincipal User user) {

        return ApiResponseDto.success(SuccessStatus.DIARY_RESPONSE_SUCCESS,
                diaryService.getDiaries(user.getUsername()));
    }

    @GetMapping("/test")
    public void test(@Parameter(hidden = true) @AuthenticationPrincipal User user){
        diaryService.checkPossibleDiary();
    }
}
