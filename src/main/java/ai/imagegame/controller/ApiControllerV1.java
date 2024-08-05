package ai.imagegame.controller;

import ai.imagegame.domain.LevelStatus;
import ai.imagegame.domain.RedisGameData;
import ai.imagegame.dto.v1.*;
import ai.imagegame.exception.BadRequestException;
import ai.imagegame.service.v1.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class ApiControllerV1 {
    private final ImageService imageService;

    @PutMapping("image-game")
    public ImageGameResponseDtoV1 imageGameV1(@RequestBody ImageGameRequestDtoV1 request) {
        if (!request.getGuessInfo().getInputLetters().isEmpty() || request.getGuessInfo().getCurrentGuess() != 0) {
            throw new BadRequestException();
        }
        RedisGameData redisGameData = imageService.randomImage(request.getGameInfo().getLevel());
        return new ImageGameResponseDtoV1(request.getGameInfo(), redisGameData);
    }

    @PutMapping("level")
    public LevelStatus levelDtoV1(@RequestBody GameInfoDtoV1 gameInfoDtoV1) {
        if (gameInfoDtoV1.getQuestions() == 0) {
            if (gameInfoDtoV1.getLevel() == this.imageService.getMaxLevel()) return LevelStatus.ALL_CLEAR;
            else return LevelStatus.CURRENT_LEVEL_CLEAR;
        }
        else throw new BadRequestException();
    }

    @PutMapping("image")
    public ImageInfoDtoV1 imageInfoV1(@RequestBody GameInfoDtoV1 gameInfoDtoV1) {
        RedisGameData redisGameData = imageService.randomImage(gameInfoDtoV1.getLevel());
        return new ImageInfoDtoV1(redisGameData.getImageInfo());
    }

    @PostMapping("guess")
    public GuessResultDtoV1 guessV1(@RequestBody GuessRequestDtoV1 request) {
        char guess = request.getGuessInfo().getCurrentGuess();
        String answer = this.imageService.getAnswer(request.getImageInfo().getUuid());
        boolean isCorrectAnswer = this.imageService.isCorrectAnswer(answer, request.getGuessInfo().getCurrentGuess(), request.getGuessInfo().getInputLetters());
        List<Integer> answerIndexList = this.imageService.getAnswerIndexList(answer, request.getGuessInfo().getCurrentGuess());
        Set<Character> inputLetters = new HashSet<>(request.getGuessInfo().getInputLetters());
        inputLetters.add(guess);

        return new GuessResultDtoV1(isCorrectAnswer, answerIndexList, inputLetters);
    }
}