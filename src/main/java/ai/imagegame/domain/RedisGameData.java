package ai.imagegame.domain;

import ai.imagegame.repository.v1.GameDataEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class RedisGameData implements Serializable {
    private int level;
    private ImageInfo imageInfo = new ImageInfo();
    private QuestionInfo questionInfo = new QuestionInfo();

    public RedisGameData(GameDataEntity gameDataEntity) {
        this.level = gameDataEntity.getLevel();

        this.imageInfo.setId(gameDataEntity.getId());
        this.imageInfo.setUuid(gameDataEntity.getUuid());
        this.imageInfo.setPcImage(gameDataEntity.getPcImage());
        this.imageInfo.setMobileImage(gameDataEntity.getMobileImage());

        this.questionInfo.setPrefix(gameDataEntity.getPrefix());
        this.questionInfo.setQuestion(gameDataEntity.getAnswer());
        this.questionInfo.setPostfix(gameDataEntity.getPostfix());
    }
}
