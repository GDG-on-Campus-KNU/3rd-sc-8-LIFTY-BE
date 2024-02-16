package gdsc.sc8.LIFTY.service;

import gdsc.sc8.LIFTY.DTO.diary.DiaryResponseDto;
import gdsc.sc8.LIFTY.domain.*;
import gdsc.sc8.LIFTY.infrastructure.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final GeminiService geminiService;
    private final EmotionRepository emotionRepository;
    private static final int MINIMUN_TO_GENERATE = 10;



    public List<DiaryResponseDto> getDiaries(String email){
        User user = userRepository.getUserByEmail(email);
        List<DiaryResponseDto> responseList = new ArrayList<>();

        List<Diary> diaryList = diaryRepository.findByUser(user);
        for(Diary diary:diaryList){
            List<String> emotions  = emotionRepository.findByDiary(diary).stream()
                    .map(Emotion::getKeyword)
                    .collect(Collectors.toList());
            System.out.println(emotions.get(0));
            responseList.add(DiaryResponseDto.toDiaryDto(diary,emotions));
        }

        return responseList;
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void checkPossibleDiary(){
        // 매일 자정 실행
        //TODO: 몇개의 대화가 모였을때 일기를 만들지 논의 필요
        LocalDate now = LocalDate.now().minusDays(1);
        List<Chat> chatList = chatRepository.findByDate(now);
        for(Chat chat:chatList){
            List<Message> messages = messageRepository.findByChat(chat);
            if (messages.size()>=MINIMUN_TO_GENERATE){
                String content = geminiService.generateDiary(messages);
                //List<Emotion> emotions = geminiService.generateEmotion();
                diaryRepository.save(new Diary(chat.getUser(),now,content));
            }

        }
    }


}
