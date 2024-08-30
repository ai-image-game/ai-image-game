package ai.imagegame.dto.v1;

import ai.imagegame.domain.QuestionInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuestionInfoDtoV1 {
    private String answer;
    private String prefix;
    private String postfix;

    public QuestionInfoDtoV1(QuestionInfo questionInfo) {
        this.answer = questionInfo.getAnswer().toLowerCase().replaceAll("[a-z']", "*");
        this.prefix = questionInfo.getPrefix();
        this.postfix = questionInfo.getPostfix();
    }
}
