package ai.imagegame.dto.v1;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GuessResponseDtoV1 {
    private GuessResultDtoV1 guessResult;
    private GameInfoDtoV1 gameInfo;
    private GameStatusInfoDtoV1 statusInfo;
    private QuestionInfoDtoV1 questionInfo;
}
