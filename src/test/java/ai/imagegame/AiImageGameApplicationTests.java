package ai.imagegame;

import ai.imagegame.repository.v1.GameDataEntityRepositoryV1;
import ai.imagegame.repository.v1.RedisGameDataV1;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
class AiImageGameApplicationTests {
	@Autowired
	private GameDataEntityRepositoryV1 gameDataEntityRepositoryV1;
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, RedisGameDataV1> hashOperations;
	private final static String IMAGE_KEY_PREFIX = "image";

	@Test
	void contextLoads() {
		/*List<GameDataEntityV1> gameDataList = gameDataEntityRepositoryV1.findAll();
		gameDataList.forEach(gameData -> {
			String key = String.join("_", IMAGE_KEY_PREFIX, String.valueOf(gameData.getLevel()));
			this.hashOperations.put(key, String.valueOf(gameData.getUuid()), new RedisGameDataV1(gameData));
		});
		String uuid = gameDataList.get(0).getUuid();
		System.out.println("#### " +this.hashOperations.randomEntry(String.join("_", IMAGE_KEY_PREFIX, uuid)));*/
	}

}
