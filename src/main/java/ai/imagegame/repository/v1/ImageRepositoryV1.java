package ai.imagegame.repository.v1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepositoryV1 extends JpaRepository<ImageInfoV1, Long> {
    @Query("SELECT MAX(i.level) FROM ImageInfoV1 i")
    int findMaxLevel();
}
