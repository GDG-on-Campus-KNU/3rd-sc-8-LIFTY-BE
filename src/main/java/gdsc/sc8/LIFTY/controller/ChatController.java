package gdsc.sc8.LIFTY.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import gdsc.sc8.LIFTY.DTO.chat.ChatRequestDto;
import gdsc.sc8.LIFTY.domain.User;
import gdsc.sc8.LIFTY.infrastructure.UserRepository;
import gdsc.sc8.LIFTY.service.ChatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Tag(name = "Chat", description = "채팅 관련 API")
public class ChatController {
    private final ChatService chatService;
    private final UserRepository userRepository;

    @PostMapping(produces = "text/event-stream")
    public ResponseEntity<SseEmitter> chat(@RequestBody ChatRequestDto request){
        User user = userRepository.findByEmail("test").get();
        SseEmitter response = chatService.generateResponse(user,request.getContent(),request.isImage());
        return ResponseEntity.ok().body(response);
    }
}
