package gdsc.sc8.LIFTY.infrastructure;

import gdsc.sc8.LIFTY.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
}
