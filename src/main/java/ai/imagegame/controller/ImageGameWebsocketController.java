package ai.imagegame.controller;

import ai.imagegame.dto.v1.ImageGameResponseDtoV1;
import ai.imagegame.service.v1.GameServiceV1;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class ImageGameWebsocketController {
    private final GameServiceV1 gameService;

    @MessageMapping("/random")
    @SendToUser("/queue/random")
    public ImageGameResponseDtoV1 random(SimpMessageHeaderAccessor messageHeaderAccessor) {
        ImageGameResponseDtoV1 response = this.gameService.getImageGameResponse(messageHeaderAccessor);
        this.gameService.addImageGameInfoToHeader(messageHeaderAccessor, response);
        return response;
    }
}
