package gdsc.sc8.LIFTY.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gdsc.sc8.LIFTY.DTO.gemini.GeminiRequestDto;
import gdsc.sc8.LIFTY.DTO.gemini.GeminiResponseDto;
import gdsc.sc8.LIFTY.config.GeminiConfig;
import gdsc.sc8.LIFTY.domain.Chat;
import gdsc.sc8.LIFTY.domain.User;
import gdsc.sc8.LIFTY.enums.Sender;
import gdsc.sc8.LIFTY.exception.ErrorStatus;
import gdsc.sc8.LIFTY.exception.model.NotFoundException;
import gdsc.sc8.LIFTY.infrastructure.ChatRepository;
import gdsc.sc8.LIFTY.infrastructure.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final GeminiConfig geminiConfig;
    private final MessageService messageService;
    private final ImageService imageService;
    private static final Long DEFAULT_TIMEOUT = 120L * 1000 * 60;


    @Transactional
    public SseEmitter generateResponse(String email, String request, Boolean isImage){
        User user = userRepository.getUserByEmail(email);
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        GeminiRequestDto requestDto;

        Chat chat = chatRepository.returnChat(user,LocalDate.now());
        messageService.saveMessage(Sender.USER, request, chat);

        updateExp(user);

        if (!isImage)
            requestDto = GeminiRequestDto.toRequestDto(messageService.makeContents(chat));
        else requestDto = imageService.toRequestDtoWithImage(request,user.getSocialId()!=null);


        Flux<GeminiResponseDto> flux = geminiConfig.geminiClient(user,isImage)
                .post()
                .body(BodyInserters.fromValue(requestDto))
                .exchangeToFlux(response -> response.bodyToFlux(GeminiResponseDto.class));

        flux.doOnNext(data -> emitAndSave(data,sseEmitter,chat))
                .doOnComplete(sseEmitter::complete)
                .doOnError(sseEmitter::completeWithError)
                .subscribe();

        return sseEmitter;
    }

    private void updateExp(User user) {
        Long exp = user.getExp();
        Long level = user.getLevel();

        if (level == 10) {
            return;
        }

        exp += 10 - (level) ;
        if (exp >= 100) {
            exp -= 100;
            level += 1;
        }
        user.updateExp(exp);
        user.updateLevel(level);
        userRepository.save(user);
    }



    private void emitAndSave(GeminiResponseDto data, SseEmitter sseEmitter, Chat chat){
        StringBuffer sb = new StringBuffer();
        try {
            String response = data.getCandidates().get(0).getContent().getParts().get(0).getText();
            sb.append(response);
            sseEmitter.send(response);

            if (data.getCandidates().get(0).getFinishReason()!=null)
                messageService.saveMessage(Sender.MODEL, sb.toString(), chat);

        } catch (IOException | NullPointerException e) {
            throw new NotFoundException(ErrorStatus.RECEIVE_RESPONSE_EXCEPTION,
                    ErrorStatus.RECEIVE_RESPONSE_EXCEPTION.getMessage());
        }
    }
}
