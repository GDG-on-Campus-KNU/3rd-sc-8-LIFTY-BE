package gdsc.sc8.LIFTY.DTO.diary;

import gdsc.sc8.LIFTY.domain.Diary;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class DiaryResponseDto {
    private LocalDate date;
    private String content;
    private List<String> keywords;

    public static DiaryResponseDto toDiaryDto(Diary diary,List<String> emotions){
        return DiaryResponseDto.builder()
                .date(diary.getDate())
                .content(diary.getContent())
                .keywords(emotions)
                .build();
    }
}
