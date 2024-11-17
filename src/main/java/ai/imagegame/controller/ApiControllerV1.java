package ai.imagegame.controller;

import ai.imagegame.dto.v1.*;
import ai.imagegame.service.v1.GameServiceV1;
import ai.imagegame.service.v1.GameStatusService1;
import ai.imagegame.service.v1.GuessServiceV1;
import ai.imagegame.util.EncDecService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
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
    public ImageGameInfoForClientDtoV1 reconnectV1(@CookieValue("savedData") String savedData) throws Exception {
        return this.encDecService.decrypt(savedData, ImageGameInfoForClientDtoV1.class);
    }

    @PostMapping("image-game/save")
    public void saveV1(@RequestBody ImageGameInfoForClientDtoV1 imageGame, HttpServletResponse response) throws Exception {
        String encryptedData = this.encDecService.encrypt(imageGame);

        Cookie cookie = new Cookie("savedData", encryptedData);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(30 * 24 * 60 * 60); //30days

        response.addCookie(cookie);
    }
}