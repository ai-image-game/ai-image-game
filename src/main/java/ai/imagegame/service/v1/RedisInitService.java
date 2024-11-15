package ai.imagegame.service.v1;

import ai.imagegame.repository.v1.GameDataEntityRepositoryV1;
import ai.imagegame.repository.v1.GameDataEntityV1;
import ai.imagegame.repository.v1.RedisGameDataV1;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("local")
public class RedisInitService {
    public RedisInitService(final RedisGameDataServiceV1 redisGameDataServiceV1,
                            final GameDataEntityRepositoryV1 gameDataEntityRepositoryV1,
                            final GameServiceV1 gameServiceV1) {
        List<GameDataEntityV1> gameDataEntityV1List = gameDataEntityRepositoryV1.findAll();
        gameDataEntityV1List.forEach(gameDataEntity -> {
            if (gameDataEntity.isVisible()) {
                //redisGameDataServiceV1.insertGameDataToRedis(gameDataEntity);
                redisGameDataServiceV1.insertGameDataToRedis(gameDataEntity.getUuid(), new RedisGameDataV1(gameDataEntity));
                redisGameDataServiceV1.insertAnswersToRedis(gameDataEntity.getUuid(), gameDataEntity.getAnswer());
            }
        });
        gameServiceV1.initAnswerMap();
    }
}
