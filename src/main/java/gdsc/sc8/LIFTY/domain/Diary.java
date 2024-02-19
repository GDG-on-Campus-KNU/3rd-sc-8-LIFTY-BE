package gdsc.sc8.LIFTY.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
    private LocalDate date;
    @Column(length = 50000)
    private String content;
    @OneToMany(mappedBy = "diary")
    private List<Emotion> emotions;

    public Diary(User user, LocalDate date, String content){
        this.user = user;
        this.date = date;
        this.content = content;
    }

}
