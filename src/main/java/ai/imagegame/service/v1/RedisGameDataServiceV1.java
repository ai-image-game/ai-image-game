package ai.imagegame.service.v1;

import ai.imagegame.dto.v1.ImageInfoDtoV1;
import ai.imagegame.repository.v1.GameDataEntityV1;
import ai.imagegame.repository.v1.RedisGameDataV1;
import ai.imagegame.repository.v1.RedisKeyV1;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisGameDataServiceV1 {
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Object> hashOperationsV1;

    public RedisGameDataV1 randomInfo(int level) {
        Map.Entry<String, Object> randomEntry = this.hashOperationsV1.randomEntry(RedisKeyV1.levelImage(level));
        if (randomEntry == null || randomEntry.getKey() == null) {
            throw new RuntimeException("Not found image and question info.");
        }
        return (RedisGameDataV1) randomEntry.getValue();
    }

    public RedisGameDataV1 randomInfo(int level, ImageInfoDtoV1 imageInfo) {
        Map.Entry<String, Object> randomEntry = this.hashOperationsV1.randomEntry(RedisKeyV1.levelImage(level));
        if (randomEntry == null || randomEntry.getKey() == null) {
            throw new RuntimeException("Not found image and question info.");
        }

        String newImgUuid = ((RedisGameDataV1) randomEntry.getValue()).getImageInfo().getUuid();
        if (imageInfo != null && newImgUuid.equals(imageInfo.getUuid())) {
            return randomInfo(level, imageInfo);
        }

        return (RedisGameDataV1) randomEntry.getValue();
    }

    public void insertAnswersToRedis(String uuid, String answer) {
        this.hashOperationsV1.put(RedisKeyV1.ANSWER, uuid, answer);
    }

    public void insertGameDataToRedis(String uuid, RedisGameDataV1 gameData) {
        this.hashOperationsV1.put(RedisKeyV1.GAME_DATA, uuid, gameData);
    }

    public Map<String, Object> getAllAnswers() {
        return this.hashOperationsV1.entries(RedisKeyV1.ANSWER);
    }

    public RedisGameDataV1 getGameData(String uuid) {
       return (RedisGameDataV1) this.hashOperationsV1.get(RedisKeyV1.GAME_DATA, uuid);
    }

    public void insertGameDataToRedis(GameDataEntityV1 gameDataEntity) {
        RedisGameDataV1 redisImageInfo = new RedisGameDataV1(gameDataEntity);
        String key = String.join("_", RedisKeyV1.GAME, String.valueOf(redisImageInfo.getLevel()));
        String hashKey = String.valueOf(redisImageInfo.getImageInfo().getUuid());
        this.hashOperationsV1.put(key, hashKey, redisImageInfo);
    }
}
