package gdsc.sc8.LIFTY.domain;

import gdsc.sc8.LIFTY.enums.Sender;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
@Entity
@Getter
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Chat chat;
    @Enumerated(EnumType.STRING)
    private Sender sender;
    @Column(length = 50000)
    private String content;
    private LocalDateTime createdAt;
}
