package gdsc.sc8.LIFTY.domain;

import gdsc.sc8.LIFTY.enums.Authority;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private String socialId;

    @Builder
    public User(String email, String password, String name, String profileUri, String socialId) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.exp = 0L;
        this.profileUri = profileUri;
        this.socialId = socialId;
        this.authority = Authority.ROLE_USER;
    }

    public void changeSocialId(String socialId) {
        this.socialId = socialId;
    }
}
