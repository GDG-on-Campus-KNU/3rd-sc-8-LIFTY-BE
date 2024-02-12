package gdsc.sc8.LIFTY.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Configuration
public class WebClientConfig {
    @Value("${gemini.secret.key}")
    private String KEY;
    @Value("${gemini.region}")
    private String REGION;
    @Value("${gemini.project.id}")
    private String PROJECT_ID;
    @Value("${gemini.access.token}")
    private String ACCESSTOKEN;
    private static final Integer DEFAULT_TIMEOUT = 1000 * 20;
    private static final Integer DEFAULT_MEMORY_SIZE = 2 * 1024 * 1024;
    HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, DEFAULT_TIMEOUT);


    @Bean
    public WebClient webClient(){

        //Stream 지원 안됨
        String quickUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key="+KEY;

        String url = "https://"+REGION+"-aiplatform.googleapis.com/v1/projects/"
                +PROJECT_ID+"/locations/"
                +REGION+"/publishers/google/models/gemini-pro-vision:streamGenerateContent?alt=sse";


        return WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION,"Bearer "+ACCESSTOKEN)
                .codecs(config -> config.defaultCodecs().maxInMemorySize(DEFAULT_MEMORY_SIZE))
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }


    @Bean
    public ConnectionProvider connectionProvider() {
        //데이터가 전달되기 전 커넥션 끊김 방지
        return ConnectionProvider.builder("http-pool")
                .maxConnections(100)
                .pendingAcquireTimeout(Duration.ofMillis(0))
                .pendingAcquireMaxCount(-1)
                .maxIdleTime(Duration.ofMillis(1000L))
                .build();
    }
}
