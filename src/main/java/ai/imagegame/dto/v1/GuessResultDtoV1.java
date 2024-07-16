package ai.imagegame.dto.v1;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class GuessResultDtoV1 {
    private char currentGuess;
    private List<Integer> answerIndexList;
    private Set<Character> inputLetters;

    public GuessResultDtoV1(char currentGuess, List<Integer> answerIndexList, Set<Character> inputLetters) {
        this.currentGuess = currentGuess;
        this.answerIndexList = answerIndexList;
        this.inputLetters = inputLetters;
    }
}