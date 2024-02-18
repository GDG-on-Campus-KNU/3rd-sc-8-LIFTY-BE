package gdsc.sc8.LIFTY.controller;

import gdsc.sc8.LIFTY.DTO.ApiResponseDto;
import gdsc.sc8.LIFTY.DTO.image.ImageResponseDto;
import gdsc.sc8.LIFTY.exception.ErrorStatus;
import gdsc.sc8.LIFTY.exception.SuccessStatus;
import gdsc.sc8.LIFTY.service.ChatImageService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/chat-image")
@RequiredArgsConstructor
@SecurityRequirement(name = "Access Token")
@Tag(name = "ChatImage", description = "채팅 이미지 관련 API")
public class ChatImageController {

    private final ChatImageService chatImageService;

    @PostMapping("/upload/image")
    public ApiResponseDto<?> upload(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @RequestParam(name = "image", required = false) MultipartFile image) throws IOException {
        if (image == null || image.isEmpty() || image.getContentType() == null
            || !image.getContentType().startsWith("image")) {
            return ApiResponseDto.error(ErrorStatus.NO_IMAGE, ErrorStatus.NO_IMAGE.getMessage());
        }
        return ApiResponseDto.success(
            SuccessStatus.IMAGE_UPLOAD_SUCCESS, chatImageService.upload(user.getUsername(), image));
    }

}
