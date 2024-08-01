package ai.imagegame.service.v1;

import ai.imagegame.dto.v1.RedisImageInfoV1;
import ai.imagegame.dto.v1.RedisKeyV1;
import ai.imagegame.repository.v1.ImageInfoV1;
import ai.imagegame.repository.v1.ImageRepositoryV1;
import jakarta.annotation.Resource;
import lombok.Getter;
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
    private Map<String, String> answerCacheMap = new HashMap<>();
    private final ImageRepositoryV1 imageRepositoryV1;
    private Set<Character> specialCharacters = new HashSet<>();
    @Getter private int maxLevel = 0;

    public void init() {
        List<ImageInfoV1> imageInfoV1List = this.imageRepositoryV1.findAll();
        imageInfoV1List.forEach(imageInfo -> {
            insertImageToRedis(imageInfo);
            this.answerCacheMap.put(imageInfo.getUuid(), imageInfo.getAnswer());
        });
        this.maxLevel = this.imageRepositoryV1.findMaxLevel();
        this.specialCharacters.addAll(List.of(' ', '\''));
    }

    private void insertImageToRedis (ImageInfoV1 imageInfo) {
        RedisImageInfoV1 redisImageInfo = new RedisImageInfoV1(imageInfo);
        String key = String.join("_", RedisKeyV1.IMAGE, String.valueOf(redisImageInfo.getLevel()));
        String hashKey = String.valueOf(redisImageInfo.getId());
        this.hashOperationsV1.putIfAbsent(key, hashKey, redisImageInfo);
    }

    public RedisImageInfoV1 randomImage(int level) {
        Map.Entry<String, RedisImageInfoV1> imagInfoEntry = this.hashOperationsV1.randomEntry(RedisKeyV1.levelImage(level));
        if (imagInfoEntry == null || imagInfoEntry.getKey() == null) {
            throw new RuntimeException("Not found image info.");
        }
        return imagInfoEntry.getValue();
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
