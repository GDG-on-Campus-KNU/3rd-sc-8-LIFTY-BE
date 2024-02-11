package gdsc.sc8.LIFTY.domain;

import gdsc.sc8.LIFTY.enums.Sender;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@Entity
@Getter
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Chat chat;
    @Enumerated(EnumType.STRING)
    private Sender sender;
    private String content;
    private LocalDateTime createdAt;
}
