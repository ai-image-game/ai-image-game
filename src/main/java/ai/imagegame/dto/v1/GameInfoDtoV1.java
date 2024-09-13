package ai.imagegame.dto.v1;

import lombok.Data;

@Data
public class GameInfoDtoV1 {
    private int level = 1;
    private int questions;
    private int correct = 0;
}
