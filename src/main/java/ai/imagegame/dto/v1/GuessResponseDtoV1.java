package ai.imagegame.dto.v1;

import lombok.Data;

@Data
public class GuessResponseDtoV1 {
    private GuessResultDtoV1 guessResult;
    private GameInfoDtoV1 gameInfo;
    private GameStatusInfoDtoV1 statusInfo;

    public GuessResponseDtoV1(GuessResultDtoV1 guessResult, GameInfoDtoV1 gameInfo, GameStatusInfoDtoV1 statusInfo) {
        this.guessResult = guessResult;
        this.statusInfo = statusInfo;
        this.gameInfo = gameInfo;
    }
}
