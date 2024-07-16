package ai.imagegame.dto.v1;

import lombok.Data;

@Data
public class GuessRequestDtoV1 {
    private ImageInfoDtoV1 imageInfo;
    private GameInfoDtoV1 gameInfo;
    private GuessInfoDtoV1 guessInfo;
}
