package gdsc.sc8.LIFTY.infrastructure;

import gdsc.sc8.LIFTY.domain.Chat;
import gdsc.sc8.LIFTY.domain.Message;
import gdsc.sc8.LIFTY.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {
    List<Message> findByChatAndCreatedAtBetween(Chat chat, LocalDateTime start, LocalDateTime end);
}
