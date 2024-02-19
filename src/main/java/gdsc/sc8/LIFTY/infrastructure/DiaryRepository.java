package gdsc.sc8.LIFTY.infrastructure;

import gdsc.sc8.LIFTY.domain.Diary;
import gdsc.sc8.LIFTY.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary,Long> {
    List<Diary> findByUserAndDateBetween(User user, LocalDate start, LocalDate end);
    List<Diary> findByUser(User user);



}
