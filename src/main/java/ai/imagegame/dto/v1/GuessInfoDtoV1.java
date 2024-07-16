package ai.imagegame.dto.v1;

import ai.imagegame.dto.GuessInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class GuessInfoDtoV1 extends GuessInfo {
    private char currentGuess;
    private Set<Character> inputLetters;
}
