package gdsc.sc8.LIFTY.controller;

import gdsc.sc8.LIFTY.DTO.ApiResponseDto;
import gdsc.sc8.LIFTY.DTO.chat.ChatRequestDto;
import gdsc.sc8.LIFTY.exception.SuccessStatus;
import gdsc.sc8.LIFTY.service.ChatService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@SecurityRequirement(name = "Access Token")
@Tag(name = "Chat", description = "채팅 관련 API")
public class ChatController {

    private final ChatService chatService;


    @PostMapping(produces = "text/event-stream")
    public ResponseEntity<SseEmitter> chat(
        @Parameter(hidden = true) @AuthenticationPrincipal User user, @RequestBody ChatRequestDto request){
        SseEmitter response = chatService.generateResponse(user.getUsername(),request.getContent(),request.isImage());
        return ResponseEntity.ok().body(response);
    }


}
