package ai.imagegame.service.v1;

import ai.imagegame.dto.v1.RedisImageInfoV1;
import ai.imagegame.dto.v1.RedisKeyV1;
import ai.imagegame.repository.v1.ImageInfoV1;
import ai.imagegame.repository.v1.ImageRepositoryV1;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ImageServiceV1 {
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, RedisImageInfoV1> hashOperationsV1;
    private Map<Long, String> answerCacheMap = new HashMap<>();
    private final ImageRepositoryV1 imageRepositoryV1;

    public void init() {
        List<ImageInfoV1> imageInfoV1List = imageRepositoryV1.findAll();
        imageInfoV1List.forEach(imageInfo -> {
            RedisImageInfoV1 redisImageInfo = new RedisImageInfoV1(imageInfo);
            String key = String.join("_", RedisKeyV1.IMAGE, String.valueOf(redisImageInfo.getLevel()));
            String hashKey = String.valueOf(redisImageInfo.getId());
            this.hashOperationsV1.putIfAbsent(key, hashKey, redisImageInfo);
            this.answerCacheMap.put(imageInfo.getId(), imageInfo.getAnswer());
        });
    }

    public RedisImageInfoV1 randomImage(int level) {
        Map.Entry<String, RedisImageInfoV1> imagInfoEntry = this.hashOperationsV1.randomEntry(RedisKeyV1.levelImage(level));
        if (imagInfoEntry == null || imagInfoEntry.getKey() == null) {
            throw new RuntimeException("Not found image info.");
        }
        return imagInfoEntry.getValue();
    }

    public String getAnswer(long id) {
        return this.answerCacheMap.get(id).toUpperCase();
    }

    public List<Integer> getAnswerIndexList(String answer, char guess) {
        List<Integer> answerIndexList = new ArrayList<>();
        IntStream.range(0, answer.length())
                .filter(i -> answer.charAt(i) == guess)
                .forEach(answerIndexList::add);
        return answerIndexList;
    }

    public boolean isCorrectAnswer(String answer, char guess, Set<Character> inputLetters) {
        for (char c : inputLetters) {
            answer = answer.replaceAll(String.valueOf(c), "");
        }
        answer = answer.replaceAll(String.valueOf(guess), "");
        return answer.isEmpty();
    }
}
