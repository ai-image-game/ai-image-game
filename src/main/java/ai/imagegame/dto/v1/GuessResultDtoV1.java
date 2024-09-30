package ai.imagegame.dto.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuessResultDtoV1 {
    private char input;
    private boolean isCorrectAnswer;
    private List<Integer> answerIndexList;
    private Set<Character> wrongLetters;
}