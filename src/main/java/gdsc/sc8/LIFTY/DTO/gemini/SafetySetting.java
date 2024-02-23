package gdsc.sc8.LIFTY.DTO.gemini;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SafetySetting {
    private String category;
    private String threshold;
}
