package ai.imagegame.config;

import ai.imagegame.service.v1.ImageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final ImageService imageService;

    @PostConstruct
    public void initRedisV1() {
        this.imageService.init();
    }
}
