package ai.imagegame.dto.v1;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class ReconnectResponseDtoV1 {
    private GameInfoDtoV1 gameInfo;
    private ImageInfoDtoV1 imageInfo;
    private QuestionInfoDtoV1 questionInfo;
    private GameStatusInfoDtoV1 statusInfo;
    private GuessResultDtoV1 guessResult;
    private List<Letters> letters;
    private String[] imgHistory;

    @Data
    @NoArgsConstructor
    private static class Letters {
        private char letter;
        private Boolean correct;
    }
}
