package gdsc.sc8.LIFTY.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import gdsc.sc8.LIFTY.DTO.gemini.GeminiRequestDto;
import gdsc.sc8.LIFTY.DTO.gemini.GeminiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {
    private static final Long DEFAULT_TIMEOUT = 120L * 1000 * 60;
    @Value("${gemini.region}")
    private String REGION;
    @Value("${gemini.project.id}")
    private String PROJECT_ID;
    @Value("${gemini.access.token}")
    private String ACCESSTOKEN;

    public SseEmitter generateResponse(String content, Boolean isImage){
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
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
                        sseEmitter.send(response);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .doOnComplete(sseEmitter::complete)
                .doOnError(sseEmitter::completeWithError)
                .subscribe();

        return sseEmitter;
    }


}
