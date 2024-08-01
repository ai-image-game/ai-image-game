package ai.imagegame.dto.v1;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class GuessResultDtoV1 {
    private List<Integer> answerIndexList;
    private Set<Character> inputLetters;
    private boolean isCorrectAnswer;

    public GuessResultDtoV1(boolean isCorrectAnswer, List<Integer> answerIndexList, Set<Character> inputLetters) {
        this.isCorrectAnswer = isCorrectAnswer;
        this.answerIndexList = answerIndexList;
        this.inputLetters = inputLetters;
    }
}