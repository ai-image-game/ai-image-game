package ai.imagegame.dto.v1;

import lombok.Data;

@Data
public class GameStatusInfoDtoV1 {
    boolean isCorrect = false;
    boolean isClear = false;
    boolean isLevelUp = false;
    boolean isGameOver = false;
    int retryCount = 0;
}
