package gdsc.sc8.LIFTY.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Emotion {
    //TODO: 정해진 키워드 내에서 선택하는 것인지, 매번 다른 키워드가 생길수 있는지 논의 필요
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Diary diary;
    private String Keyword;
}
