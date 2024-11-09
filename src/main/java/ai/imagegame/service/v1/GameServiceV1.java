package ai.imagegame.service.v1;

import ai.imagegame.dto.v1.*;
import ai.imagegame.repository.v1.GameDataEntityRepositoryV1;
import ai.imagegame.repository.v1.GameDataEntityV1;
import ai.imagegame.repository.v1.RedisGameDataV1;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GameServiceV1 {
    private final static int UNKNOWN_LEVEL = 0;
    private final static int MIN_LEVEL = 1;
    private final static int QUESTIONS = 10;
    public final static int MAX_RETRY_COUNT = 3;
    private final RedisGameDataServiceV1 redisGameDataServiceV1;
    private final GameDataEntityRepositoryV1 gameDataEntityRepositoryV1;
    private final GuessServiceV1 guessServiceV1;
    private final ObjectMapper objectMapper;
    @Value("${secret.key}")
    private String secretKey;

    public void init() {
        /*List<GameDataEntityV1> gameDataEntityV1List = this.gameDataEntityRepositoryV1.findAll();
        gameDataEntityV1List.forEach(gameDataEntity -> {
            if (gameDataEntity.isVisible()) {
                redisGameDataServiceV1.insertGameDataToRedis(gameDataEntity);
                this.redisGameDataServiceV1.insertAnswerToRedis(gameDataEntity.getUuid(), gameDataEntity.getAnswer());
            }
        });*/

        Map<String, Object> answersMap = this.redisGameDataServiceV1.getAllAnswers();
        this.guessServiceV1.setAnswerCacheMap(answersMap);
    }

    public RedisGameDataV1 get(String uuid) {
        GameDataEntityV1 gameData = this.gameDataEntityRepositoryV1.findByUuid(uuid);
        return new RedisGameDataV1(gameData);
    }

    public ImageGameRequestDtoV1 getRequestByHeader(SimpMessageHeaderAccessor messageHeaderAccessor) {
        if (messageHeaderAccessor.getSessionAttributes() == null
                || messageHeaderAccessor.getSessionAttributes().get("gameInfo") == null) return null;

        ImageGameRequestDtoV1 imageGameRequestDto = new ImageGameRequestDtoV1();
        imageGameRequestDto.setGameInfo((GameInfoDtoV1) messageHeaderAccessor.getSessionAttributes().get("gameInfo"));
        imageGameRequestDto.setQuestionInfo((QuestionInfoDtoV1) messageHeaderAccessor.getSessionAttributes().get("questionInfo"));
        imageGameRequestDto.setImageInfo((ImageInfoDtoV1) messageHeaderAccessor.getSessionAttributes().get("imageInfo"));
        return imageGameRequestDto;
    }

    public GameInfoDtoV1 reduceRetryCount(SimpMessageHeaderAccessor messageHeaderAccessor) {
        if (messageHeaderAccessor.getSessionAttributes() == null
                || messageHeaderAccessor.getSessionAttributes().get("gameInfo") == null) {
            throw new RuntimeException("GameInfo is not defined in session.");
        }

        GameInfoDtoV1 gameInfo = ((GameInfoDtoV1) messageHeaderAccessor.getSessionAttributes().get("gameInfo"));
        int retry = gameInfo.getRetry();
        gameInfo.setRetry(retry == 0 ? 0 : retry - 1);

        messageHeaderAccessor.getSessionAttributes().put("gameInfo", gameInfo);
        return gameInfo;
    }

    public void addImageGameInfoToHeader(SimpMessageHeaderAccessor messageHeaderAccessor, ImageGameRequestDtoV1 request) {
        if (request != null && messageHeaderAccessor.getSessionAttributes() != null) {
            messageHeaderAccessor.getSessionAttributes().put("gameInfo", request.getGameInfo());
            messageHeaderAccessor.getSessionAttributes().put("imageInfo", request.getImageInfo());
            messageHeaderAccessor.getSessionAttributes().put("questionInfo", request.getQuestionInfo());
        }
    }

    public void addImageGameInfoToHeader(SimpMessageHeaderAccessor messageHeaderAccessor, ImageGameResponseDtoV1 response) {
        if (response != null && messageHeaderAccessor.getSessionAttributes() != null) {
            messageHeaderAccessor.getSessionAttributes().put("gameInfo", response.getGameInfo());
            messageHeaderAccessor.getSessionAttributes().put("imageInfo", response.getImageInfo());
            messageHeaderAccessor.getSessionAttributes().put("questionInfo", response.getQuestionInfo());
        }
    }

    public void addGameInfo(SimpMessageHeaderAccessor messageHeaderAccessor, GameInfoDtoV1 gameInfo) {
        if (messageHeaderAccessor.getSessionAttributes() != null) {
            messageHeaderAccessor.getSessionAttributes().put("gameInfo", gameInfo);
        }
    }

    public ImageGameResponseDtoV1 getResponse(ImageGameRequestDtoV1 request) {
        ImageGameResponseDtoV1 response = new ImageGameResponseDtoV1();
        response.setStatusInfo(new GameStatusInfoDtoV1());
        response.setGameInfo(request.getGameInfo() == null ? new GameInfoDtoV1(MIN_LEVEL, QUESTIONS) : getGameInfo(request.getGameInfo(), response.getStatusInfo()));

        RedisGameDataV1 redisGameData;
        if (request.getImageInfo() != null) {
            redisGameData = redisGameDataServiceV1.randomInfo(response.getGameInfo().getLevel(), request.getImageInfo());
        } else {
            redisGameData = redisGameDataServiceV1.randomInfo(response.getGameInfo().getLevel());
        }
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
            gameInfo.setQuestions(request.getQuestions() - 1);
            gameInfo.setCorrects(request.getCorrects() + 1);
            gameInfo.setRetry(MAX_RETRY_COUNT);
        } else {
            if (isLevelUpFirstQuestion(request)) {
                initGameInfo(gameInfo, request);
            } else if (status.isCorrect()) {
                gameInfo.setLevel(request.getLevel());
                gameInfo.setQuestions(request.getQuestions() - 1);
                gameInfo.setCorrects(request.getCorrects() + 1);
                gameInfo.setRetry(MAX_RETRY_COUNT);
            } else if (isSharedQuestion(request)) {
                initGameInfo(gameInfo, request);
            } else {
                gameInfo.setCorrects(request.getCorrects());
                gameInfo.setQuestions(request.getQuestions());
                gameInfo.setLevel(request.getLevel());
                gameInfo.setRetry(request.getRetry());
            }
        }
        return gameInfo;
    }

    private boolean isLevelUpFirstQuestion(GameInfoDtoV1 request) {
        return request.getLevel() != 1
                && request.getQuestions() == 0
                && request.getCorrects() == QUESTIONS;
    }

    private boolean isSharedQuestion(GameInfoDtoV1 request) {
        return request.getLevel() == 1
                && request.getQuestions() == 0
                && request.getCorrects() == 0;
    }

    private void initGameInfo(GameInfoDtoV1 gameInfo, GameInfoDtoV1 request) {
        gameInfo.setLevel(request.getLevel());
        gameInfo.setQuestions(QUESTIONS);
        gameInfo.setCorrects(0);
        gameInfo.setRetry(MAX_RETRY_COUNT);
    }

    public ReconnectResponseDtoV1 decryptData(String encryptedData) throws Exception {
        byte[] decodedKey = Arrays.copyOf(secretKey.getBytes(StandardCharsets.UTF_8), 16); // 16바이트 키 길이 맞추기
        SecretKeySpec secretKeySpec = new SecretKeySpec(decodedKey, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        String result = new String(decryptedBytes, StandardCharsets.UTF_8);
        ReconnectResponseDtoV1 response = this.objectMapper.readValue(result, ReconnectResponseDtoV1.class);
        return response;
    }
}