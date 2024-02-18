package gdsc.sc8.LIFTY.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChatImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUri;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public ChatImage(String imageUri, User user) {
        this.imageUri = imageUri;
        this.user = user;
    }
}
