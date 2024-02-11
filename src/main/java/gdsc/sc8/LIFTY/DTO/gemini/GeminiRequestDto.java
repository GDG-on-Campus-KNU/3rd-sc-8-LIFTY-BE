package gdsc.sc8.LIFTY.DTO.gemini;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class GeminiRequestDto implements Serializable {
    private List<Content> contents;
    @Data
    @AllArgsConstructor
    private static class Content{
        private String role;
        private Part parts;
    }
    @Data
    @AllArgsConstructor
    private static class Part{
        private String text;
    }
    public GeminiRequestDto(String text){
        Part part = new Part(text);
        Content content = new Content("USER",part);
        List<Content> contents = new ArrayList<>();
        contents.add(content);
        this.contents = contents;
    }
}
