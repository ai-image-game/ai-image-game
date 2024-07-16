package ai.imagegame.dto.v1;

public final class RedisKeyV1 {
    public final static String IMAGE = "v1_image";
    public static String levelImage (int level) {
        return String.join("_", IMAGE, String.valueOf(level));
    }
}
