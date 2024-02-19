package gdsc.sc8.LIFTY.service;

import gdsc.sc8.LIFTY.DTO.diary.DiaryResponseDto;
import gdsc.sc8.LIFTY.domain.*;
import gdsc.sc8.LIFTY.exception.ErrorStatus;
import gdsc.sc8.LIFTY.exception.model.NotFoundException;
import gdsc.sc8.LIFTY.infrastructure.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final GenerativeModelService generativeModelService;
    private final EmotionRepository emotionRepository;

    //TODO: 몇개의 대화가 모였을때 일기를 만들지 논의 필요
    private static final int MINIMUN_TO_GENERATE = 10;


    public List<DiaryResponseDto> getDiaries(String email){
        User user = userRepository.getUserByEmail(email);
        List<DiaryResponseDto> responseList = new ArrayList<>();

        List<Diary> diaryList = diaryRepository.findByUser(user);
        for(Diary diary:diaryList){
            List<String> emotions  = emotionRepository.findByDiary(diary).stream()
                    .map(Emotion::getKeyword)
                    .collect(Collectors.toList());
            responseList.add(DiaryResponseDto.toDiaryDto(diary,emotions));
        }

        return responseList;
    }


    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkPossibleDiary(){
        // 매일 자정 실행
        LocalDate now = LocalDate.now().minusDays(1);
        List<Chat> chatList = chatRepository.findByDate(now);

        for(Chat chat:chatList){
            List<Message> messages = messageRepository.findByChat(chat);

            if (messages.size()>=MINIMUN_TO_GENERATE){
                String content = generativeModelService.generateDiary(messages);
                Diary diary = diaryRepository.save(new Diary(chat.getUser(),now,content));

                List<String> keywords = generativeModelService.generateEmotion(content);

                for(String keyword:keywords)
                    emotionRepository.save(new Emotion(diary,keyword));
            }
        }
    }


}
