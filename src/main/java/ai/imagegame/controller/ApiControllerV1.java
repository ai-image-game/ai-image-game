package ai.imagegame.controller;

import ai.imagegame.dto.v1.ImageGameRequestDtoV1;
import ai.imagegame.dto.v1.ImageGameResponseDtoV1;
import ai.imagegame.exception.BadRequestException;
import ai.imagegame.service.v1.GameServiceV1;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class ApiControllerV1 {
    private final GameServiceV1 gameService;

    @PutMapping("image-game")
    public ImageGameResponseDtoV1 imageGameV1(@RequestBody ImageGameRequestDtoV1 request) {
        if (request != null
                && request.getQuestionInfo().getAnswer() != null
                && request.getQuestionInfo().getAnswer().contains("*")) {
            throw new BadRequestException();
        }
        return this.gameService.getResponse(request);
    }

    @GetMapping("image-game/{uuid}")
    public ImageGameResponseDtoV1 imageGameV1(@PathVariable("uuid") String uuid) {
        return this.gameService.getResponse(uuid);
    }
}