package ai.imagegame.dto.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import static ai.imagegame.service.v1.GameServiceV1.MAX_RETRY_COUNT;

@Data
@NoArgsConstructor
public class GameInfoDtoV1 {
    private int level;
    private int questions;
    private int corrects = 0;
    private int retry = MAX_RETRY_COUNT;

    public GameInfoDtoV1(int level, int questions) {
        this.level = level;
        this.questions = questions;
    }

    @JsonProperty("usedRetry")
    public int getUsedRetry() {
        return MAX_RETRY_COUNT - retry;
    }
}
