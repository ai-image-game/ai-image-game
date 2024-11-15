package ai.imagegame.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Profile("local")
@EnableJpaRepositories(basePackages = "ai.imagegame.repository")
public class JpaConfig {
}
