package ai.imagegame.controller;

import ai.imagegame.dto.LevelStatus;
import ai.imagegame.dto.v1.*;
import ai.imagegame.exception.BadRequestException;
import ai.imagegame.service.v1.ImageServiceV1;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class ApiControllerV1 {
    private final ImageServiceV1 imageServiceV1;

    @GetMapping("game-info")
    public GameInfoDtoV1 gameInfoV1() {
        return new GameInfoDtoV1();
    }

    @PutMapping("level")
    public LevelStatus levelDtoV1(@RequestBody GameInfoDtoV1 gameInfoDtoV1) {
        if (gameInfoDtoV1.getQuestions() == 0) {
            if (gameInfoDtoV1.getLevel() == this.imageServiceV1.getMaxLevel()) return LevelStatus.ALL_CLEAR;
            else return LevelStatus.CURRENT_LEVEL_CLEAR;
        }
        else throw new BadRequestException();
    }

    @PutMapping("image")
    public ImageInfoDtoV1 imageInfoV1(@RequestBody GameInfoDtoV1 gameInfoDtoV1) {
        RedisImageInfoV1 redisImageInfoV1 = imageServiceV1.randomImage(gameInfoDtoV1.getLevel());
        ImageInfoDtoV1 imageInfoDtoV1 = new ImageInfoDtoV1();
        imageInfoDtoV1.setImageId(redisImageInfoV1.getUuid());
        imageInfoDtoV1.setPcImage(redisImageInfoV1.getPcImage());
        imageInfoDtoV1.setMobileImage(redisImageInfoV1.getMobileImage());
        return imageInfoDtoV1;
    }

    @PostMapping("guess")
    public GuessResultDtoV1 guessV1(@RequestBody GuessRequestDtoV1 request) {
        char guess = request.getGuessInfo().getCurrentGuess();
        String answer = this.imageServiceV1.getAnswer(request.getImageInfo().getImageId());
        boolean isCorrectAnswer = this.imageServiceV1.isCorrectAnswer(answer, request.getGuessInfo().getCurrentGuess(), request.getGuessInfo().getInputLetters());
        List<Integer> answerIndexList = this.imageServiceV1.getAnswerIndexList(answer, request.getGuessInfo().getCurrentGuess());
        Set<Character> inputLetters = new HashSet<>(request.getGuessInfo().getInputLetters());
        inputLetters.add(guess);

        return new GuessResultDtoV1(isCorrectAnswer, answerIndexList, inputLetters);
    }
}