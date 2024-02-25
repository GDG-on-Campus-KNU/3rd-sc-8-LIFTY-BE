package gdsc.sc8.LIFTY.domain.prompt;

public class DiaryPrompt {
    private String prompt;
    private DiaryPrompt(){
        prompt = "Convert the content of this conversation into a diary entry based on the main content of the dialogue not including write Date.";
    }
    private static class HOLDER{
        public static final DiaryPrompt INSTANCE = new DiaryPrompt();
    }
    public static DiaryPrompt getInstance(){
        return HOLDER.INSTANCE;
    }
    public String getPrompt(){
        return prompt;
    }
}
