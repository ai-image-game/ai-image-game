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
    public void initV1(SimpMessageHeaderAccessor messageHeaderAccessor, @Payload ImageGameRequestDtoV1 request) {
        this.gameService.addImageGameInfoToHeader(messageHeaderAccessor, request);
    }

    @MessageMapping("/next")
    @SendToUser("/queue/next")
    public ImageGameResponseDtoV1 randomV1(SimpMessageHeaderAccessor messageHeaderAccessor) {
        ImageGameRequestDtoV1 request = this.gameService.getRequestByHeader(messageHeaderAccessor);
        ImageGameResponseDtoV1 response = this.gameService.getResponse(request);
        this.gameService.addImageGameInfoToHeader(messageHeaderAccessor, response);
        return response;
    }

    @MessageMapping("/guess")
    @SendToUser("/queue/guess")
    public GuessResponseDtoV1 guessV1(@Payload GuessInfoDtoV1 guessInfo, SimpMessageHeaderAccessor messageHeaderAccessor) {
        GuessRequestDtoV1 request = guessService.getGuessInfoByHeader(messageHeaderAccessor, guessInfo);
        GuessResultDtoV1 guessResult = guessService.guess(request);
        GameStatusInfoDtoV1 gameStatusInfo = this.gameStatusService.getStatus(request.getGameInfo(), guessResult);
        GameInfoDtoV1 gameInfo = this.gameService.getGameInfo(request.getGameInfo(), gameStatusInfo);
        QuestionInfoDtoV1 questionInfo = this.guessService.getUpdatedQuestionInfo(guessResult, request.getQuestionInfo());
        GuessResponseDtoV1 response = GuessResponseDtoV1.builder()
                                        .guessResult(guessResult).gameInfo(gameInfo).statusInfo(gameStatusInfo).questionInfo(questionInfo)
                                    .build();
        this.guessService.addGuessInfoToHeader(messageHeaderAccessor, response);
        this.gameService.addGameInfo(messageHeaderAccessor, gameInfo);
        return response;
    }

    @MessageMapping("/retry")
    @SendToUser("/queue/retry")
    public RetryResponseDto1 retryV1(SimpMessageHeaderAccessor messageHeaderAccessor) {
        GameInfoDtoV1 gameInfo = this.gameService.reduceRetryCount(messageHeaderAccessor);
        GuessResultDtoV1 guessResult = this.guessService.initGuessResult(messageHeaderAccessor);
        QuestionInfoDtoV1 questionInfo = this.guessService.initQuestionInfo(messageHeaderAccessor);

        return new RetryResponseDto1(gameInfo, guessResult, questionInfo);
    }
}
