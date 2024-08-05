package ai.imagegame.domain;

import lombok.Data;

@Data
public class QuestionInfo {
    private String question;
    private String prefix;
    private String postfix;
}
