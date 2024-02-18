package gdsc.sc8.LIFTY.DTO.gemini;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenerationConfig implements Serializable {
    private float temperature=0;
//    private float topP;
//    private float topK;
//    private float candidateCount;
//    private float maxOutputTokens;
//    private List<String> stopSequences;
}
