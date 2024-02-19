package gdsc.sc8.LIFTY.config;

import gdsc.sc8.LIFTY.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
@Component
@RequiredArgsConstructor
public class GeminiConfig {
    //TODO: 엑세스 토큰 설정
    //TODO: 사용자 유형에 따른 서비스
    @Value("${gemini.region}")
    private String REGION;
    @Value("${gemini.project.id}")
    private String PROJECT_ID;
    @Value("${gemini.secret.key}")
    private String KEY;
    private final WebClientConfig webClientConfig;

    public WebClient geminiClient(User user,boolean isImage){
        String quickUrlWithImage = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro-vision:generateContent?key="+KEY;
        String quickUrlOnlyText = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key="+KEY;

        String url = "https://"+REGION+"-aiplatform.googleapis.com/v1/projects/"
                +PROJECT_ID+"/locations/"
                +REGION+"/publishers/google/models/gemini-pro-vision:streamGenerateContent?alt=sse";

        if (user.getSocialId()==null && isImage){
            return webClientConfig.webClient().mutate()
                    .baseUrl(quickUrlWithImage)
                    .build();
        } else if (user.getSocialId()==null) {
            return webClientConfig.webClient().mutate()
                    .baseUrl(quickUrlOnlyText)
                    .build();
        } else{
            return webClientConfig.webClient().mutate()
                    .baseUrl(url)
                    .defaultHeader(HttpHeaders.AUTHORIZATION,"Bearer "+user.getGeminiToken())
                    .build();
        }
    }
}