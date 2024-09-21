package ai.imagegame.controller;

import ai.imagegame.dto.v1.*;
import ai.imagegame.service.v1.GameServiceV1;
import ai.imagegame.service.v1.GameStatusService1;
import ai.imagegame.service.v1.GuessServiceV1;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class ImageGameWebsocketController {
    private final GameServiceV1 gameService;
    private final GuessServiceV1 guessService;
    private final GameStatusService1 gameStatusService;

    @MessageMapping("/init")
    @SendToUser("/queue/init")
    public void init(SimpMessageHeaderAccessor messageHeaderAccessor, @Payload ImageGameRequestDtoV1 request) {
        this.gameService.addImageGameInfoToHeader(messageHeaderAccessor, request);
    }

    @MessageMapping("/guess")
    @SendToUser("/queue/guess")
    public GuessResponseDtoV1 guessV1(@Payload GuessInfoDtoV1 guessInfo, SimpMessageHeaderAccessor messageHeaderAccessor) {
        GuessRequestDtoV1 request = guessService.getGuessInfoByHeader(messageHeaderAccessor, guessInfo);
        GuessResultDtoV1 guessResult = guessService.guess(request);
        GameStatusInfoDtoV1 gameStatusInfo = this.gameStatusService.getStatus(request.getGameInfo(), guessResult.isCorrectAnswer());
        GameInfoDtoV1 gameInfo = this.gameService.getGameInfo(request.getGameInfo(), gameStatusInfo);
        this.guessService.addGuessInfoToHeader(messageHeaderAccessor, guessResult);
        this.gameService.addGameInfo(messageHeaderAccessor, gameInfo);
        return new GuessResponseDtoV1(guessResult, gameInfo, gameStatusInfo);
    }
}
