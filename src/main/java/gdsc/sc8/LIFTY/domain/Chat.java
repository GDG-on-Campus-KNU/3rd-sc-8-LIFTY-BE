package gdsc.sc8.LIFTY.domain;

import gdsc.sc8.LIFTY.enums.Status;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;


@Entity
@Getter
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    private LocalDate date;
    @ColumnDefault("false")
    private boolean isUsedInDiary;
    public Chat(User user, LocalDate date){
        this.user = user;
        this.date = date;
    }

}
