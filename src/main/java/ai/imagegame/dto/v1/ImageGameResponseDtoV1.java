package ai.imagegame.dto.v1;

import ai.imagegame.repository.v1.RedisGameDataV1;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageGameResponseDtoV1 {
    private GameInfoDtoV1 gameInfo;
    private ImageInfoDtoV1 imageInfo;
    private QuestionInfoDtoV1 question;

    public ImageGameResponseDtoV1(GameInfoDtoV1 gameInfo, RedisGameDataV1 redisGameData) {
        this.gameInfo = gameInfo;
        this.imageInfo = new ImageInfoDtoV1(redisGameData.getImageInfo());
        this.question = new QuestionInfoDtoV1(redisGameData.getQuestionInfo());
    }
}
