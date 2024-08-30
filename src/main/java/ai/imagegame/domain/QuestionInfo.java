package ai.imagegame.domain;

import lombok.Data;

@Data
public class QuestionInfo {
    private String answer;
    private String prefix;
    private String postfix;
}
