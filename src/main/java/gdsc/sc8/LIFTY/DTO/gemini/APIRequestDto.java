package gdsc.sc8.LIFTY.DTO.gemini;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class APIRequestDto implements Serializable {
    private List<Context> contents;
    @Data
    @AllArgsConstructor
    private static class Context{
        private List<Part> parts;
        @Data
        @AllArgsConstructor
        private static class Part{
            private String text;
        }
    }

    public APIRequestDto(String text){
        List<Context> contents = new ArrayList<>();
        List<Context.Part> parts = new ArrayList<>();
        Context.Part part = new Context.Part(text);

        parts.add(part);
        contents.add(new Context(parts));
        this.contents=contents;
    }

}



