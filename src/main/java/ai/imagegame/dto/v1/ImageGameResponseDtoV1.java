package ai.imagegame.dto.v1;

import ai.imagegame.domain.RedisGameData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageGameResponseDtoV1 {
    private GameInfoDtoV1 gameInfo;
    private ImageInfoDtoV1 imageInfo;
    private QuestionInfoDtoV1 question;

    public ImageGameResponseDtoV1(GameInfoDtoV1 gameInfo, RedisGameData redisGameData) {
        this.gameInfo = gameInfo;
        this.imageInfo = new ImageInfoDtoV1(redisGameData.getImageInfo());
        this.question = new QuestionInfoDtoV1(redisGameData.getQuestionInfo());
    }
}
