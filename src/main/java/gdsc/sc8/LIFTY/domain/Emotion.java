package gdsc.sc8.LIFTY.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Emotion {
    //TODO: 정해진 키워드 내에서 선택하는 것인지, 매번 다른 키워드가 생길수 있는지 논의 필요
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="diary_id")
    private Diary diary;
    private String keyword;

    public Emotion(Diary diary,String keyword){
        this.diary = diary;
        this.keyword = keyword;
    }
}
