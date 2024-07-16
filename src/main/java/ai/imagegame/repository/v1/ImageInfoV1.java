package ai.imagegame.repository.v1;

import ai.imagegame.repository.ImageInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.ZonedDateTime;


@Entity
@Table(name = "image")
@Getter @Setter
@NoArgsConstructor
public class ImageInfoV1 extends ImageInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 300) private String pcImage;
    @Column(nullable = false, length = 300) private String mobileImage;
    @Column(nullable = false) private int level;
    @Column(length = 30) private String prefix;
    @Column(nullable = false, length = 30) private String answer;
    @Column(length = 30) private String postfix;
    @CreatedDate
    private ZonedDateTime createDate;
}