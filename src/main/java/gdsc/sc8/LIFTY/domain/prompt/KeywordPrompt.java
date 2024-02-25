package gdsc.sc8.LIFTY.domain.prompt;

public class KeywordPrompt {
    private String prompt;
    private KeywordPrompt(){
        prompt = "Extract positive emotional keywords from this text and display them in hashtag form.";
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
