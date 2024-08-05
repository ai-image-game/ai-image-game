package ai.imagegame.repository.v1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GameDataEntityRepositoryV1 extends JpaRepository<GameDataEntityV1, Long> {
    @Query("SELECT MAX(g.level) FROM GameDataEntityV1 g")
    int findMaxLevel();
}
