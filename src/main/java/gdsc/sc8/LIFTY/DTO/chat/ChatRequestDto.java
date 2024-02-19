package gdsc.sc8.LIFTY.DTO.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ChatRequestDto {
    private String request;
    @JsonProperty("isImage")
    private Boolean isImage;
}
