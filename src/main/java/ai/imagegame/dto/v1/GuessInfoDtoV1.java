package ai.imagegame.dto.v1;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class GuessInfoDtoV1 {
    private Set<Character> wrongLetters;
}
