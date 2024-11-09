package ai.imagegame.service.v1;

import ai.imagegame.dto.v1.GameInfoDtoV1;
import ai.imagegame.dto.v1.GameStatusInfoDtoV1;
import ai.imagegame.dto.v1.GuessResultDtoV1;
import ai.imagegame.repository.v1.GameDataEntityRepositoryV1;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class GameStatusService1 {
    private final int maxLevel;
    private final int MAX_WRONG_LETTERS = 6;

    public GameStatusService1(GameDataEntityRepositoryV1 gameDataEntityRepositoryV1) {
        this.maxLevel = gameDataEntityRepositoryV1.findMaxLevel();
    }

    public GameStatusInfoDtoV1 getStatus(GameInfoDtoV1 request, GuessResultDtoV1 guessResult) {
        GameStatusInfoDtoV1 status = new GameStatusInfoDtoV1();
        status.setCorrect(guessResult.isCorrectAnswer());

        if (guessResult.isCorrectAnswer()) {
            if (isClear(request.getLevel(), request.getQuestions())) {
                status.setClear(true);
            } else if (isLevelUp(request.getQuestions())) {
                status.setLevelUp(true);
            }
        } else {
            if (guessResult.getWrongLetters().size() >= MAX_WRONG_LETTERS) {
                status.setGameOver(true);
            }
        }
        return status;
    }

    private boolean isClear(int level, int questions) {
        return level == maxLevel && (questions - 1 == 0);
    }

    private boolean isLevelUp(int questions) {
        return (questions - 1 == 0);
    }
}
