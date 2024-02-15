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
public class GeminiRequestDto implements Serializable {
    private List<Content> contents;
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
        @Data
        @AllArgsConstructor
        public static class FileData{
            private String mimeType;
            private String fileUrl;
        }
        public Part(String text){
            this.text=text;
        }
        public Part(FileData fileData){
            this.fileData=fileData;
        }
    }

    //TODO:멀티모달 contents 객체 생성
    public GeminiRequestDto(String text,String imageUrl){
        Part.FileData fileData = new Part.FileData("image/**",imageUrl);
        Part part1 = new Part(text);
        Part part2 = new Part(fileData);
        List<Part> parts = new ArrayList<>();
        parts.add(part1);
        parts.add(part2);
        Content content = new Content("USER",parts);
        List<Content> contents = new ArrayList<>();
        contents.add(content);
        this.contents = contents;
    }

    public static GeminiRequestDto toRequestDto(List<Message> messages){
        List<GeminiRequestDto.Content> contents = new ArrayList<>();
        for(Message message:messages)
            contents.add(GeminiRequestDto.toContent(message.getSender(),message.getContent()));
        return new GeminiRequestDto(contents);
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

}
