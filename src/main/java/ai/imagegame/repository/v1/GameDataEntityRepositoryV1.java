package ai.imagegame.repository.v1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GameDataEntityRepositoryV1 extends JpaRepository<GameDataEntity, Long> {
    @Query("SELECT MAX(g.level) FROM GameDataEntity g")
    int findMaxLevel();
}
