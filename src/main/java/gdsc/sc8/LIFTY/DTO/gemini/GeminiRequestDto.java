package gdsc.sc8.LIFTY.DTO.gemini;

import com.fasterxml.jackson.annotation.JsonInclude;
import gdsc.sc8.LIFTY.domain.Message;
import gdsc.sc8.LIFTY.enums.Sender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeminiRequestDto implements Serializable {
    private List<Content> contents;
    private GenerationConfig generationConfig;
    private List<SafetySetting> safetySettings;
    @Data
    @AllArgsConstructor
    public static class Content{
        private String role;
        private List<Part> parts;
    }
    @Data
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Part{
        private String text;
        private FileData fileData;
        private InlineData inlineData;
        @Data
        @AllArgsConstructor
        public static class FileData{
            private String mimeType;
            private String fileUri;
        }
        @Data
        @AllArgsConstructor
        public static class InlineData{
            private String mimeType;
            private String data;
        }
        public Part(String text){
            this.text=text;
        }
        public Part(FileData fileData){
            this.fileData=fileData;
        }
        public Part(InlineData inlineData){
            this.inlineData=inlineData;
        }
    }

    public GeminiRequestDto(List<Content> contents, GenerationConfig generationConfig){
        this.contents = contents;
        this.generationConfig=generationConfig;
    }


    public static GeminiRequestDto toRequestDto(List<Message> messages){
        List<GeminiRequestDto.Content> contents = new ArrayList<>();
        for(Message message:messages)
            contents.add(GeminiRequestDto.toContent(message.getSender(),message.getContent()));

        return new GeminiRequestDto(contents, new GenerationConfig());
    }


    public static Content toContent(Sender role, String text){
        List<Part> parts = new ArrayList<>();
        if (role==Sender.USER) {
            parts.add(new Part(text));
            return new Content("USER",parts);
        }
        else {
            parts.add(new Part(text));
            return new Content("MODEL",parts);
        }
    }

    public static GeminiRequestDto toRequestDto(String text){
        List<GeminiRequestDto.Content> contents = new ArrayList<>();
        List<Part> parts = new ArrayList<>();
        parts.add(new Part(text));
        contents.add(new Content("USER",parts));

        List<SafetySetting> safetySettings = new ArrayList<>();
        safetySettings.add(new SafetySetting("HARM_CATEGORY_SEXUALLY_EXPLICIT","BLOCK_NONE"));
        safetySettings.add(new SafetySetting("HARM_CATEGORY_HATE_SPEECH","BLOCK_NONE"));
        safetySettings.add(new SafetySetting("HARM_CATEGORY_HARASSMENT","BLOCK_NONE"));
        safetySettings.add(new SafetySetting("HARM_CATEGORY_DANGEROUS_CONTENT","BLOCK_NONE"));

        return new GeminiRequestDto(contents, new GenerationConfig(), safetySettings);
    }

}
