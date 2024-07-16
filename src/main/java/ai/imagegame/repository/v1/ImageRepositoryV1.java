package ai.imagegame.repository.v1;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepositoryV1 extends JpaRepository<ImageInfoV1, Long> {
}
