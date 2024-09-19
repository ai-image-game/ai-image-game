package ai.imagegame.dto.v1;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameInfoDtoV1 {
    private int level;
    private int questions;
    private int corrects = 0;

    public GameInfoDtoV1(int level, int questions) {
        this.level = level;
        this.questions = questions;
    }
}
