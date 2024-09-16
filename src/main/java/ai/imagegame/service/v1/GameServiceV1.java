package ai.imagegame.service.v1;

import ai.imagegame.dto.v1.*;
import ai.imagegame.repository.v1.RedisGameDataV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameServiceV1 {
    private final static int UNKNOWN_LEVEL = 0;
    private final static int MIN_LEVEL = 1;
    private final static int MAX_LEVEL = 2;
    private final static int QUESTIONS = 10;
    private final ImageService imageService;

    public ImageGameResponseDtoV1 getResponse(ImageGameRequestDtoV1 request) {
        ImageGameResponseDtoV1 response = new ImageGameResponseDtoV1();
        response.setStatusInfo(request == null ? new GameStatusInfoDtoV1() : getStatus(request));
        response.setGameInfo(request == null ? new GameInfoDtoV1(MIN_LEVEL, QUESTIONS) : getGameInfo(request, response.getStatusInfo()));

        RedisGameDataV1 redisGameData = imageService.randomImage(response.getGameInfo().getLevel());
        response.setImageInfo(new ImageInfoDtoV1(redisGameData.getImageInfo()));
        response.setQuestionInfo(new QuestionInfoDtoV1(redisGameData.getQuestionInfo()));

        return response;
    }

    public ImageGameResponseDtoV1 getResponse(String uuid) {
        ImageGameResponseDtoV1 response = new ImageGameResponseDtoV1();
        response.setStatusInfo(new GameStatusInfoDtoV1());
        response.setGameInfo(new GameInfoDtoV1(UNKNOWN_LEVEL, 0));

        RedisGameDataV1 redisGameData = imageService.get(uuid);
        response.setImageInfo(new ImageInfoDtoV1(redisGameData.getImageInfo()));
        response.setQuestionInfo(new QuestionInfoDtoV1(redisGameData.getQuestionInfo()));
        return response;
    }

    private GameStatusInfoDtoV1 getStatus(ImageGameRequestDtoV1 request) {
        GameStatusInfoDtoV1 status = new GameStatusInfoDtoV1();
        if (isCorrect(request.getQuestionInfo().getAnswer())) {
            if (isClear(request.getGameInfo().getLevel(), request.getGameInfo().getQuestions())) {
                status.setClear(true);
            }  else if (isLevelUp(request.getGameInfo().getQuestions())) {
                status.setLevelUp(true);
            }
        }
        return status;
    }

    private boolean isCorrect(String answer) {
        return !answer.isEmpty() && !answer.contains("*");
    }

    private boolean isClear(int level, int questions) {
        return level == MAX_LEVEL && (questions - 1 == 0);
    }

    private boolean isLevelUp(int questions) {
        return (questions - 1 == 0);
    }

    private GameInfoDtoV1 getGameInfo(ImageGameRequestDtoV1 request, GameStatusInfoDtoV1 status) {
        GameInfoDtoV1 gameInfo = new GameInfoDtoV1();
        if (status.isLevelUp()) {
            gameInfo.setLevel(request.getGameInfo().getLevel() + 1);
            gameInfo.setQuestions(QUESTIONS);
            gameInfo.setCorrects(0);
        } else {
            if (request.getGameInfo().getLevel() == 1
                    && request.getGameInfo().getQuestions() == 0
                    && request.getGameInfo().getCorrects() == 0) {
                gameInfo.setQuestions(QUESTIONS);
                gameInfo.setCorrects(0);
            } else {
                gameInfo.setQuestions(request.getGameInfo().getQuestions() - 1);
                gameInfo.setCorrects(request.getGameInfo().getCorrects() + 1);
            }
            gameInfo.setLevel(request.getGameInfo().getLevel());
        }
        return gameInfo;
    }
}
