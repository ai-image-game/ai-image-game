package ai.imagegame.repository.v1;

public final class RedisKeyV1 {
    public final static String GAME = "game_v1";
    public final static String ANSWER = "answer_v1";
    public final static String GAME_DATA = "game_data_v1";

    public static String levelImage (int level) {
        return String.join("_", GAME, String.valueOf(level));
    }
}
