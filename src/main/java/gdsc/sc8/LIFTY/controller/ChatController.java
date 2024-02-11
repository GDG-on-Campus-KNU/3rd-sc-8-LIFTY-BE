package gdsc.sc8.LIFTY.controller;
import gdsc.sc8.LIFTY.service.ChatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Chat", description = "채팅 관련 API")
public class ChatController {
    private final ChatService chatService;

    @PostMapping("")
    public void signup() {
        chatService.callGemini();
    }
}
