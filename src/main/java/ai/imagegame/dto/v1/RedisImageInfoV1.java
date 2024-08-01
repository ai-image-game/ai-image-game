package ai.imagegame.dto.v1;

import ai.imagegame.repository.v1.ImageInfoV1;
import lombok.Data;

@Data
public class RedisImageInfoV1 {
    private long id;
    private String uuid;
    private String pcImage;
    private String mobileImage;
    private int level;
    private String prefix;
    private String answer;
    private String postfix;

    public RedisImageInfoV1() {
    }

    public RedisImageInfoV1(ImageInfoV1 imageInfoV1) {
        this.id = imageInfoV1.getId();
        this.uuid = imageInfoV1.getUuid();
        this.pcImage = imageInfoV1.getPcImage();
        this.mobileImage = imageInfoV1.getMobileImage();
        this.level = imageInfoV1.getLevel();
        this.prefix = imageInfoV1.getPrefix();
        this.answer = imageInfoV1.getAnswer();
        this.postfix = imageInfoV1.getPostfix();
    }
}
