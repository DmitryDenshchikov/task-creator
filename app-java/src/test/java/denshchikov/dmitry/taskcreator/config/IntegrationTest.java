package denshchikov.dmitry.taskcreator.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

import static org.testcontainers.containers.wait.strategy.WaitAllStrategy.Mode.WITH_MAXIMUM_OUTER_TIMEOUT;

@Testcontainers
public interface IntegrationTest {

  @Container
  PostgreSQLContainer<?> postgresContainer =
      new PostgreSQLContainer<>("postgres:latest")
          .waitingFor(
              new WaitAllStrategy(WITH_MAXIMUM_OUTER_TIMEOUT)
                  .withStartupTimeout(Duration.ofSeconds(90))
                  .withStrategy(
                      Wait.forLogMessage(
                          ".*database system is ready to accept connections.*\\s", 2))
                  .withStrategy(Wait.forListeningPort()));

  @DynamicPropertySource
  static void datasourceProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.username", postgresContainer::getUsername);
    registry.add("spring.datasource.password", postgresContainer::getPassword);
    registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
  }
}
