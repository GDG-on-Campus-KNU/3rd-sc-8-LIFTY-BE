package gdsc.sc8.LIFTY.infrastructure;

import gdsc.sc8.LIFTY.domain.Chat;
import gdsc.sc8.LIFTY.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat,Long> {
    Optional<Chat> findByUserAndDate(User user, LocalDate date);
    List<Chat> findByDate(LocalDate date);

    default Chat returnChat (User user, LocalDate today) {
        return this.findByUserAndDate(user,today)
                .orElseGet(()-> this.save(new Chat(user,today)));
    }

}
