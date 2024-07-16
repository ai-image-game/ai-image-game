package ai.imagegame.config;

import ai.imagegame.service.v1.ImageServiceV1;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final ImageServiceV1 imageServiceV1;

    @PostConstruct
    public void initRedisV1() {
        this.imageServiceV1.init();
    }
}
