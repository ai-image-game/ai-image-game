package ai.imagegame.dto.v1;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RetryResponseDto1 {
    private GameInfoDtoV1 gameInfo;
    private GuessResultDtoV1 guessResult;
    private QuestionInfoDtoV1 questionInfo;
}
