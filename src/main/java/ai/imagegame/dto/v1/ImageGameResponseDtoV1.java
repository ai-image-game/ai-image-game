package ai.imagegame.dto.v1;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageGameResponseDtoV1 {
    private GameInfoDtoV1 gameInfo;
    private ImageInfoDtoV1 imageInfo;
    private QuestionInfoDtoV1 question;
    private GameStatusInfoDtoV1 status;
}
