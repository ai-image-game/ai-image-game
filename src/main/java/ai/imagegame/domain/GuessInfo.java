package ai.imagegame.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class GuessInfo {
    private char currentGuess;
    private Set<Character> inputLetters;
}
