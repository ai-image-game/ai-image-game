package ai.imagegame.service.v1;

import ai.imagegame.dto.v1.*;
import ai.imagegame.repository.v1.GameDataEntityRepositoryV1;
import ai.imagegame.repository.v1.GameDataEntityV1;
import ai.imagegame.repository.v1.RedisGameDataV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceV1 {
    private final static int UNKNOWN_LEVEL = 0;
    private final static int MIN_LEVEL = 1;
    private final static int QUESTIONS = 10;
    private final RedisGameDataServiceV1 redisGameDataServiceV1;
    private final GameDataEntityRepositoryV1 gameDataEntityRepositoryV1;
    private final GuessServiceV1 guessServiceV1;
    private final GameStatusService1 gameStatusService1;

    public void init() {
        List<GameDataEntityV1> gameDataEntityV1List = this.gameDataEntityRepositoryV1.findAll();
        gameDataEntityV1List.forEach(gameDataEntity -> {
            redisGameDataServiceV1.insertGameDataToRedis(gameDataEntity);
            this.guessServiceV1.addAnswerCacheMap(gameDataEntity);
        });
    }

    public RedisGameDataV1 get(String uuid) {
        GameDataEntityV1 gameData = this.gameDataEntityRepositoryV1.findByUuid(uuid);
        return new RedisGameDataV1(gameData);
    }

    public ImageGameResponseDtoV1 getResponse(ImageGameRequestDtoV1 request) {
        ImageGameResponseDtoV1 response = new ImageGameResponseDtoV1();
        response.setStatusInfo(request == null ? new GameStatusInfoDtoV1() : gameStatusService1.getStatus(request.getGameInfo()));
        response.setGameInfo(request == null ? new GameInfoDtoV1(MIN_LEVEL, QUESTIONS) : getGameInfo(request.getGameInfo(), response.getStatusInfo()));

        RedisGameDataV1 redisGameData = redisGameDataServiceV1.randomInfo(response.getGameInfo().getLevel());
        response.setImageInfo(new ImageInfoDtoV1(redisGameData.getImageInfo()));
        response.setQuestionInfo(new QuestionInfoDtoV1(redisGameData.getQuestionInfo()));

        return response;
    }

    public ImageGameResponseDtoV1 getResponse(String uuid) {
        ImageGameResponseDtoV1 response = new ImageGameResponseDtoV1();
        response.setStatusInfo(new GameStatusInfoDtoV1());
        response.setGameInfo(new GameInfoDtoV1(UNKNOWN_LEVEL, 0));

        RedisGameDataV1 redisGameData = get(uuid);
        response.setImageInfo(new ImageInfoDtoV1(redisGameData.getImageInfo()));
        response.setQuestionInfo(new QuestionInfoDtoV1(redisGameData.getQuestionInfo()));
        return response;
    }

    public GameInfoDtoV1 getGameInfo(GameInfoDtoV1 request, GameStatusInfoDtoV1 status) {
        GameInfoDtoV1 gameInfo = new GameInfoDtoV1();
        if (status.isLevelUp()) {
            gameInfo.setLevel(request.getLevel() + 1);
            gameInfo.setQuestions(QUESTIONS);
            gameInfo.setCorrects(0);
        } else {
            if (status.isCorrect()) {
                gameInfo.setLevel(request.getLevel());
                gameInfo.setQuestions(request.getQuestions() - 1);
                gameInfo.setCorrects(request.getCorrects() + 1);
            } else if (request.getLevel() == 1
                    && request.getQuestions() == 0
                    && request.getCorrects() == 0) {
                gameInfo.setLevel(request.getLevel());
                gameInfo.setQuestions(QUESTIONS);
                gameInfo.setCorrects(0);
            } else {
                gameInfo.setCorrects(request.getCorrects());
                gameInfo.setQuestions(request.getQuestions());
                gameInfo.setLevel(request.getLevel());
            }
        }
        return gameInfo;
    }
}