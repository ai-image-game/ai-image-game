package ai.imagegame.repository.v1;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.ZonedDateTime;


@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "game_data")
public class GameDataEntityV1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String uuid;
    @Column(nullable = false, length = 300) private String pcImage;
    @Column(nullable = false, length = 300) private String mobileImage;
    @Column(nullable = false) private int level;
    @Column(length = 30) private String prefix;
    @Column(nullable = false, length = 30) private String answer;
    @Column(length = 30) private String postfix;
    private boolean visible;
    @CreatedDate
    private ZonedDateTime createDate;
}