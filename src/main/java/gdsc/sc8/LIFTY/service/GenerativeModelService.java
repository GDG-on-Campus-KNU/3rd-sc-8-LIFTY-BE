package gdsc.sc8.LIFTY.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gdsc.sc8.LIFTY.DTO.gemini.GeminiRequestDto;
import gdsc.sc8.LIFTY.DTO.gemini.GeminiResponseDto;
import gdsc.sc8.LIFTY.config.GeminiConfig;
import gdsc.sc8.LIFTY.domain.Emotion;
import gdsc.sc8.LIFTY.domain.Message;
import gdsc.sc8.LIFTY.domain.User;
import gdsc.sc8.LIFTY.domain.prompt.DiaryPrompt;
import gdsc.sc8.LIFTY.domain.prompt.KeywordPrompt;
import gdsc.sc8.LIFTY.exception.ErrorStatus;
import gdsc.sc8.LIFTY.exception.model.NotFoundException;
import gdsc.sc8.LIFTY.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenerativeModelService {
    private final GeminiConfig geminiConfig;
    private final UserRepository userRepository;

    public String invokeModel(GeminiRequestDto requestDto) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(requestDto);
            System.out.println(jsonString);
        }catch (JsonProcessingException e){

        }


        User admin = userRepository.getUserByEmail("test");
        GeminiResponseDto result = geminiConfig.geminiClient(admin,false)
                .post()
                .body(BodyInserters.fromValue(requestDto))
                .retrieve()
                .bodyToMono(GeminiResponseDto.class)
                .onErrorResume(WebClientResponseException.class,
                        ex -> ex.getRawStatusCode() == 404 ? Mono.empty() : Mono.error(ex))
                .block();

        if (result.getCandidates()==null)
            throw new NotFoundException(ErrorStatus.RECEIVE_RESPONSE_EXCEPTION,
                    ErrorStatus.RECEIVE_RESPONSE_EXCEPTION.getMessage());
        return result.getCandidates().get(0).getContent().getParts().get(0).getText();
    }

    public String generateDiary(List<Message> messages){
        String request = DiaryPrompt.getInstance().getPrompt()+"\n\n";
        for (Message message:messages){
            request+=(message.getSender()+": "+message.getContent()+"\n");
        }

        return invokeModel(GeminiRequestDto.toRequestDto(request));
    }

    public List<String> generateEmotion(String diary){
        String request = KeywordPrompt.getInstance().getPrompt()
                            +"\n\n"+diary;

        String response = invokeModel(GeminiRequestDto.toRequestDto(request));
        String[] result = response.split("#");

        return Arrays.asList(result);
    }
}
