package ai.imagegame.service.v1;

import ai.imagegame.repository.v1.RedisGameDataV1;
import ai.imagegame.repository.v1.RedisKeyV1;
import ai.imagegame.repository.v1.GameDataEntityV1;
import ai.imagegame.repository.v1.GameDataEntityRepositoryV1;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ImageService {
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, RedisGameDataV1> hashOperationsV1;
    private Map<String, String> answerCacheMap = new HashMap<>();
    private final GameDataEntityRepositoryV1 gameDataEntityRepositoryV1;
    private Set<Character> specialCharacters = new HashSet<>();
    @Getter private int maxLevel = 0;

    public void init() {
        List<GameDataEntityV1> gameDataEntityV1List = this.gameDataEntityRepositoryV1.findAll();
        gameDataEntityV1List.forEach(gameDataEntity -> {
            insertGameDataToRedis(gameDataEntity);
            this.answerCacheMap.put(gameDataEntity.getUuid(), gameDataEntity.getAnswer());
        });
        this.maxLevel = this.gameDataEntityRepositoryV1.findMaxLevel();
        this.specialCharacters.add(' ');
    }

    private void insertGameDataToRedis(GameDataEntityV1 gameDataEntity) {
        RedisGameDataV1 redisImageInfo = new RedisGameDataV1(gameDataEntity);
        String key = String.join("_", RedisKeyV1.GAME, String.valueOf(redisImageInfo.getLevel()));
        String hashKey = String.valueOf(redisImageInfo.getImageInfo().getUuid());
        this.hashOperationsV1.put(key, hashKey, redisImageInfo);
    }

    public RedisGameDataV1 randomImage(int level) {
        Map.Entry<String, RedisGameDataV1> imagInfoEntry = this.hashOperationsV1.randomEntry(RedisKeyV1.levelImage(level));
        if (imagInfoEntry == null || imagInfoEntry.getKey() == null) {
            throw new RuntimeException("Not found image info.");
        }
        return imagInfoEntry.getValue();
    }

    public RedisGameDataV1 get(String uuid) {
        GameDataEntityV1 gameData = this.gameDataEntityRepositoryV1.findByUuid(uuid);
        return new RedisGameDataV1(gameData);
    }

    public String getAnswer(String id) {
        return this.answerCacheMap.get(id).toLowerCase();
    }

    public List<Integer> getAnswerIndexList(String answer, char guess) {
        List<Integer> answerIndexList = new ArrayList<>();
        IntStream.range(0, answer.length())
                .filter(i -> answer.charAt(i) == guess)
                .forEach(answerIndexList::add);
        return answerIndexList;
    }

    public boolean isCorrectAnswer(String answer, char guess, Set<Character> inputLetters) {
        char[] answerLetters = answer.toCharArray();
        Set<Character> answerSet = new HashSet<>();
        for (char c : answerLetters) {
            answerSet.add(c);
        }

        answerSet.removeAll(specialCharacters);
        answerSet.removeAll(inputLetters);
        answerSet.remove(guess);
        return answerSet.isEmpty();
    }

}
