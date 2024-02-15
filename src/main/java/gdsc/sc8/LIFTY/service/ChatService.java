package gdsc.sc8.LIFTY.service;

import gdsc.sc8.LIFTY.DTO.gemini.GeminiRequestDto;
import gdsc.sc8.LIFTY.DTO.gemini.GeminiResponseDto;
import gdsc.sc8.LIFTY.config.GeminiConfig;
import gdsc.sc8.LIFTY.config.WebClientConfig;
import gdsc.sc8.LIFTY.domain.Chat;
import gdsc.sc8.LIFTY.domain.User;
import gdsc.sc8.LIFTY.enums.Sender;
import gdsc.sc8.LIFTY.infrastructure.ChatRepository;
import gdsc.sc8.LIFTY.infrastructure.UserRepository;
import gdsc.sc8.LIFTY.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final GeminiConfig geminiConfig;
    private final MessageService messageService;
    private final UserRepository userRepository;
    private static final Long DEFAULT_TIMEOUT = 120L * 1000 * 60;


    public SseEmitter generateResponse(String email, String content, Boolean isImage) {
        User user = userRepository.getUserByEmail(email);
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        Chat chat = returnChat(user,LocalDate.now());
        messageService.saveMessage(Sender.USER, content, chat);


        Flux<GeminiResponseDto> flux = geminiConfig.geminiClient(user)
                .post()
                .body(BodyInserters.fromValue(GeminiRequestDto.toRequestDto(content,isImage)))
                .exchangeToFlux(response -> response.bodyToFlux(GeminiResponseDto.class));

        flux.doOnNext(data -> emitAndSave(data,sseEmitter,chat))
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

    public void emitAndSave(GeminiResponseDto data, SseEmitter sseEmitter, Chat chat){
        StringBuffer sb = new StringBuffer();
        try {
            if (data.getCandidates().get(0).getContent().getParts()!=null){
                String response = data.getCandidates().get(0).getContent().getParts().get(0).getText();
                sb.append(response);
                sseEmitter.send(response);
            }

            if (data.getCandidates().get(0).getFinishReason()!=null)
                messageService.saveMessage(Sender.MODEL, sb.toString(), chat);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
