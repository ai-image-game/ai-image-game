package ai.imagegame.controller;

import ai.imagegame.dto.v1.ImageGameInfoForClientDtoV1;
import ai.imagegame.dto.v1.ImageGameRequestDtoV1;
import ai.imagegame.dto.v1.ImageGameResponseDtoV1;
import ai.imagegame.service.v1.GameServiceV1;
import ai.imagegame.service.v1.GameStatusService1;
import ai.imagegame.service.v1.GuessServiceV1;
import ai.imagegame.util.CookieUtils;
import ai.imagegame.util.EncDecService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class ApiControllerV1 {
    private final GameServiceV1 gameService;
    private final GuessServiceV1 guessService;
    private final GameStatusService1 gameStatusService;
    private final EncDecService encDecService;

    @PutMapping("image-game")
    public ImageGameResponseDtoV1 imageGameV1(@RequestBody ImageGameRequestDtoV1 request) {
        return this.gameService.getResponse(request);
    }

    @GetMapping("image-game/{uuid}")
    public ImageGameResponseDtoV1 imageGameV1(@PathVariable("uuid") String uuid) {
        return this.gameService.getResponse(uuid);
    }

    @GetMapping("image-game/reconnect")
    public ImageGameInfoForClientDtoV1 reconnectV1(@CookieValue("savedData") String savedData, HttpServletResponse response) throws Exception {
        ImageGameInfoForClientDtoV1 imageGameInfo =  this.encDecService.decrypt(savedData, ImageGameInfoForClientDtoV1.class);
        if (!StringUtils.hasText(imageGameInfo.getImageInfo().getUuid())) {
            CookieUtils.removeCookie("savedData", response);
            return null;
        }
        return imageGameInfo;
    }

    @PostMapping("image-game/save")
    public void saveV1(@CookieValue(value = "savedData", required = false) String savedData, @RequestBody ImageGameInfoForClientDtoV1 imageGame, HttpServletResponse response) throws Exception {
        if (savedData != null) {
            ImageGameInfoForClientDtoV1 oldData = encDecService.decrypt(savedData, ImageGameInfoForClientDtoV1.class);
            //if (!this.gameService.verify(oldData, imageGame)) throw new Exception("Invalid data");
        }

        String encryptedData = this.encDecService.encrypt(imageGame);

        CookieUtils.addCookie("savedData", encryptedData, response);
    }
}