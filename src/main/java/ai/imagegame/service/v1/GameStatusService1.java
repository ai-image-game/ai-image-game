package ai.imagegame.service.v1;

import ai.imagegame.dto.v1.GameInfoDtoV1;
import ai.imagegame.dto.v1.GameStatusInfoDtoV1;
import ai.imagegame.repository.v1.GameDataEntityRepositoryV1;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class GameStatusService1 {
    private int maxLevel = 0;

    public GameStatusService1(GameDataEntityRepositoryV1 gameDataEntityRepositoryV1) {
        this.maxLevel = gameDataEntityRepositoryV1.findMaxLevel();
    }

    public GameStatusInfoDtoV1 getStatus(GameInfoDtoV1 request) {
        return getStatus(request, false);
    }

    public GameStatusInfoDtoV1 getStatus(GameInfoDtoV1 request, boolean isCorrect) {
        GameStatusInfoDtoV1 status = new GameStatusInfoDtoV1();
        status.setCorrect(isCorrect);

        if (isCorrect) {
            if (isClear(request.getLevel(), request.getQuestions())) {
                status.setClear(true);
            }  else if (isLevelUp(request.getQuestions())) {
                status.setLevelUp(true);
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
