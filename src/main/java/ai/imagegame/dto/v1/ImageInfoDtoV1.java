package ai.imagegame.dto.v1;

import lombok.Data;

@Data
public class ImageInfoDtoV1 {
    private long imageId;
    private String pcImage;
    private String mobileImage;
}
