package ai.imagegame.dto.v1;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class GuessInfoDtoV1 {
    private String input;
    private Set<Character> wrongLetters = new HashSet<>();
}
