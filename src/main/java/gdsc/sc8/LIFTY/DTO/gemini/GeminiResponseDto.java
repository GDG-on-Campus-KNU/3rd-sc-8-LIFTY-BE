package gdsc.sc8.LIFTY.DTO.gemini;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GeminiResponseDto implements Serializable {
    private List<Candidate> candidates;
    @Data
    public static class Candidate{
        private Content content;
        @Data
        public static class Content{
            private String role;
            private List<Part> parts;
            @Data
            public static class Part{
                private String text;
            }
        }
        private String finishReason;
        private List<SafetyRating> safetyRatings;
        @Data
        public static class SafetyRating{
            private String category;
            private String probability;
            private boolean blocked;
        }

        private CitationMetadata citationMetadata;
        @Data
        public static class CitationMetadata{
            private List<Citation> citations;
            @Data
            public static class Citation{
                private int startIndex;
                private int endIndex;
                private String uri;
                private String title;
                private String license;
                private PublicationDate publicationDate;
                @Data
                public static class PublicationDate{
                    private int year;
                    private int month;
                    private int day;
                }
            }
        }

    }
    private UsageMetadata usageMetadata;
    @Data
    public static class UsageMetadata{
        private int promptTokenCount;
        private int candidatesTokenCount;
        private int totalTokenCount;
    }
}
