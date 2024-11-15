package ai.imagegame.service.v1;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("live")
@RequiredArgsConstructor
public class AnswerMapInitService {
    private final GameServiceV1 gameServiceV1;

    @PostConstruct
    public void initRedisV1() {
        this.gameServiceV1.initAnswerMap();
    }
}
