package gdsc.sc8.LIFTY.infrastructure;

import gdsc.sc8.LIFTY.domain.Diary;
import gdsc.sc8.LIFTY.domain.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmotionRepository extends JpaRepository<Emotion,Long> {
    List<Emotion> findByDiary(Diary diary);
}
