package ai.imagegame.dto.v1;

import lombok.Data;

import java.util.Set;

@Data
public class AnswerCacheV1 {
    private String answer;
    private Set<String> answerSet;
}
