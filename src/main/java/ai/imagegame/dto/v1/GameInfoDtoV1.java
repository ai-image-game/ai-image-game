package ai.imagegame.dto.v1;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameInfoDtoV1 {
    private int level = 1;
    private int questions;
    private int corrects = 0;

    public GameInfoDtoV1(int questions) {
        this.questions = questions;
    }
}
