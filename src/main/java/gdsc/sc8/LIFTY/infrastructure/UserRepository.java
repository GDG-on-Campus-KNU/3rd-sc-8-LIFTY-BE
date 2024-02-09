package gdsc.sc8.LIFTY.infrastructure;

import gdsc.sc8.LIFTY.domain.User;
import gdsc.sc8.LIFTY.exception.ErrorStatus;
import gdsc.sc8.LIFTY.exception.model.NotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    default User getUserByEmail(String email) {
        return this.findByEmail(email).orElseThrow(
            () -> new NotFoundException(ErrorStatus.USER_NOT_FOUND,
                ErrorStatus.USER_NOT_FOUND.getMessage()));
    }
}
