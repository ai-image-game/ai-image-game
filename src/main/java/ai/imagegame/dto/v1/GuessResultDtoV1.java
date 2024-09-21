package ai.imagegame.dto.v1;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class GuessResultDtoV1 {
    private char input;
    private boolean isCorrectAnswer;
    private List<Integer> answerIndexList;
    private Set<Character> wrongLetters;

    public GuessResultDtoV1(char input, boolean isCorrectAnswer, List<Integer> answerIndexList, Set<Character> wrongLetters) {
        this.input = input;
        this.isCorrectAnswer = isCorrectAnswer;
        this.answerIndexList = answerIndexList;
        this.wrongLetters = wrongLetters;
    }
}