package gdsc.sc8.LIFTY.domain.prompt;

public class KeywordPrompt {
    private String prompt;
    private KeywordPrompt(){
        prompt = "Tokenize the hashtags of this text by using classified emotions in this text just show word.";
    }
    private static class HOLDER{
        public static final KeywordPrompt INSTANCE = new KeywordPrompt();
    }
    public static KeywordPrompt getInstance(){
        return HOLDER.INSTANCE;
    }
    public String getPrompt(){
        return prompt;
    }
}
