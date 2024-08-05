package ai.imagegame.dto.v1;

import ai.imagegame.domain.ImageInfo;
import lombok.Data;

@Data
public class GuessRequestDtoV1 {
    private ImageInfo imageInfo;
    private GameInfoDtoV1 gameInfo;
    private GuessInfoDtoV1 guessInfo;
}
