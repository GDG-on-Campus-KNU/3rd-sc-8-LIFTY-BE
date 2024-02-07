package gdsc.sc8.LIFTY.domain;

import gdsc.sc8.LIFTY.enums.Authority;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String name;
    private Long exp;
    private String profileUri;
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public User(String email, String password, String name, Long exp, String profileUri, Authority authority) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.exp = exp;
        this.profileUri = profileUri;
        this.authority = authority;
    }
}
