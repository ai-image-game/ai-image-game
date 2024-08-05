package ai.imagegame.dto.v1;

import ai.imagegame.domain.ImageInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageInfoDtoV1 {
    private String uuid;
    private String pcImage;
    private String mobileImage;

    public ImageInfoDtoV1(ImageInfo imageInfo) {
        this.uuid = imageInfo.getUuid();
        this.pcImage = imageInfo.getPcImage();
        this.mobileImage = imageInfo.getMobileImage();
    }
}
