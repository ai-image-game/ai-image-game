package ai.imagegame;

import ai.imagegame.repository.v1.ImageInfoV1;
import ai.imagegame.repository.v1.ImageRepositoryV1;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;

import java.util.List;

@SpringBootTest
class AiImageGameApplicationTests {
	@Autowired
	private ImageRepositoryV1 imageRepositoryV1;
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, ImageInfoV1> hashOperations;
	private final static String IMAGE_KEY_PREFIX = "image";

	@Test
	void contextLoads() {
		List<ImageInfoV1> imageInfoV1List = imageRepositoryV1.findAll();
		imageInfoV1List.forEach(imageInfo -> {
			String key = String.join("_", IMAGE_KEY_PREFIX, String.valueOf(imageInfo.getLevel()));
			this.hashOperations.put(key, String.valueOf(imageInfo.getUuid()), imageInfo);
		});
		String uuid = imageInfoV1List.get(0).getUuid();
		System.out.println("#### " +this.hashOperations.randomEntry(String.join("_", IMAGE_KEY_PREFIX, uuid)));
	}

}
