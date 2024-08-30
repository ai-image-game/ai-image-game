package ai.imagegame.repository.v1;

import ai.imagegame.domain.ImageInfo;
import ai.imagegame.domain.QuestionInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class RedisGameDataV1 implements Serializable {
    private int level;
    private ImageInfo imageInfo = new ImageInfo();
    private QuestionInfo questionInfo = new QuestionInfo();

    public RedisGameDataV1(GameDataEntityV1 gameDataEntity) {
        this.level = gameDataEntity.getLevel();

        this.imageInfo.setId(gameDataEntity.getId());
        this.imageInfo.setUuid(gameDataEntity.getUuid());
        this.imageInfo.setPcImage(gameDataEntity.getPcImage());
        this.imageInfo.setMobileImage(gameDataEntity.getMobileImage());

        this.questionInfo.setPrefix(gameDataEntity.getPrefix());
        this.questionInfo.setAnswer(gameDataEntity.getAnswer());
        this.questionInfo.setPostfix(gameDataEntity.getPostfix());
    }
}
