package gdsc.sc8.LIFTY.infrastructure;

import gdsc.sc8.LIFTY.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message,Long> {
}
