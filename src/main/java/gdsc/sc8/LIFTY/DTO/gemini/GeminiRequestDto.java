package gdsc.sc8.LIFTY.DTO.gemini;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    public GeminiRequestDto(String text){
        Part part = new Part(text);
        List<Part> parts = new ArrayList<>();
        parts.add(part);
        Content content = new Content("USER",parts);
        List<Content> contents = new ArrayList<>();
        contents.add(content);
        this.contents = contents;
    }
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

    public static GeminiRequestDto toRequestDto(String content, Boolean isImage){
        if (isImage) return new GeminiRequestDto("explain image",content);
        else return new GeminiRequestDto(content);

    }

}
