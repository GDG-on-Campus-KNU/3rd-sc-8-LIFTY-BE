package gdsc.sc8.LIFTY.service;

import gdsc.sc8.LIFTY.domain.Emotion;
import gdsc.sc8.LIFTY.domain.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeminiService {

    public String generateDiary(List<Message> messages){

        return "생성된 일기 content";
    }

    public List<Emotion> generateEmotion(){
        // 감정 리스트

        return new ArrayList<>();
    }
}
