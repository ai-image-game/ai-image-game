package ai.imagegame.domain;

import lombok.Data;

@Data
public class ImageInfo {
    private long id;
    private String uuid;
    private String pcImage;
    private String mobileImage;
    private String snsImage;
}
