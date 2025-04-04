package ai.imagegame.dto.v1;

import ai.imagegame.domain.QuestionInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuestionInfoDtoV1 {
    private String maskedAnswer;
    private String prefix;
    private String postfix;

    public QuestionInfoDtoV1(QuestionInfo questionInfo) {
        this.maskedAnswer = maskAnswer(questionInfo.getAnswer());
        this.prefix = questionInfo.getPrefix();
        this.postfix = questionInfo.getPostfix();
    }

    public String maskAnswer(String answer) {
        return answer.toLowerCase().replaceAll("[a-z']", "*");
    }
}
