package gdsc.sc8.LIFTY.service;

import gdsc.sc8.LIFTY.DTO.gemini.GeminiRequestDto;
import gdsc.sc8.LIFTY.DTO.gemini.GeminiResponseDto;
import gdsc.sc8.LIFTY.domain.Chat;
import gdsc.sc8.LIFTY.domain.User;
import gdsc.sc8.LIFTY.enums.Sender;
import gdsc.sc8.LIFTY.infrastructure.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final MessageService messageService;
    private final ChatRepository chatRepository;
    private static final Long DEFAULT_TIMEOUT = 120L * 1000 * 60;
    @Value("${gemini.region}")
    private String REGION;
    @Value("${gemini.project.id}")
    private String PROJECT_ID;
    @Value("${gemini.access.token}")
    private String ACCESSTOKEN;

    public SseEmitter generateResponse(User user, String content, Boolean isImage){
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        Chat chat = returnChat(user,LocalDate.now());
        messageService.saveMessage(Sender.USER, content, chat);
        StringBuffer sb = new StringBuffer();

        String url = "https://"+REGION+"-aiplatform.googleapis.com/v1/projects/"
                +PROJECT_ID+"/locations/"
                +REGION+"/publishers/google/models/gemini-pro-vision:streamGenerateContent?alt=sse";


        WebClient.create()
                .post().uri(url)
                .header("Authorization","Bearer "+ACCESSTOKEN)
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue(GeminiRequestDto.toRequestDto(content,isImage)))
                .exchangeToFlux(response -> response.bodyToFlux(GeminiResponseDto.class))
                .doOnNext(data -> {
                    try {
                        String response = data.getCandidates().get(0).getContent().getParts().get(0).getText();
                        sb.append(response);
                        sseEmitter.send(response);

                        if (data.getCandidates().get(0).getFinishReason()!=null)
                            messageService.saveMessage(Sender.MODEL, sb.toString(), chat);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .doOnComplete(sseEmitter::complete)
                .doOnError(sseEmitter::completeWithError)
                .subscribe();

        return sseEmitter;
    }

    public Chat returnChat(User user, LocalDate today){
        Optional<Chat> chat = chatRepository.findByUserAndDate(user,today);
        if (chat.isPresent())
            return chat.get();

        return chatRepository.save(new Chat(user,today));
    }


}
