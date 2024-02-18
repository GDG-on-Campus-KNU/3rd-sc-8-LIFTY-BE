package gdsc.sc8.LIFTY.infrastructure;

import gdsc.sc8.LIFTY.domain.ChatImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatImageRepository extends JpaRepository<ChatImage, Long> {

}
