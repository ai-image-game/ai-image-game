package ai.imagegame.controller;

import ai.imagegame.dto.v1.*;
import ai.imagegame.service.v1.GameServiceV1;
import ai.imagegame.service.v1.GameStatusService1;
import ai.imagegame.service.v1.GuessServiceV1;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class ApiControllerV1 {
    private final GameServiceV1 gameService;
    private final GuessServiceV1 guessService;
    private final GameStatusService1 gameStatusService;

    @PutMapping("image-game")
    public ImageGameResponseDtoV1 imageGameV1(@RequestBody ImageGameRequestDtoV1 request) {
        return this.gameService.getResponse(request);
    }

    @GetMapping("image-game/{uuid}")
    public ImageGameResponseDtoV1 imageGameV1(@PathVariable("uuid") String uuid) {
        return this.gameService.getResponse(uuid);
    }

    @PutMapping("guess")
    public GuessResponseDtoV1 guessV1(@RequestBody GuessRequestDtoV1 request) {
        GuessResultDtoV1 guessResult = guessService.guess(request);
        GameStatusInfoDtoV1 gameStatusInfo = this.gameStatusService.getStatus(request.getGameInfo(), guessResult.isCorrectAnswer());
        GameInfoDtoV1 gameInfo = this.gameService.getGameInfo(request.getGameInfo(), gameStatusInfo);
        return GuessResponseDtoV1.builder()
                .guessResult(guessResult).gameInfo(gameInfo).statusInfo(gameStatusInfo)
                .build();
    }
}