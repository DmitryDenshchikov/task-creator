package denshchikov.dmitry.taskcreator.config

import org.junit.jupiter.api.BeforeAll
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.containers.wait.strategy.WaitAllStrategy
import org.testcontainers.containers.wait.strategy.WaitAllStrategy.Mode.WITH_MAXIMUM_OUTER_TIMEOUT
import java.time.Duration

interface IntegrationTest {

    companion object {

        val container = PostgreSQLContainer("postgres:latest")
            .waitingFor(
                WaitAllStrategy(WITH_MAXIMUM_OUTER_TIMEOUT)
                    .withStartupTimeout(Duration.ofSeconds(1))
                    .withStrategy(Wait.forLogMessage(".*database system is ready to accept connections.*\\s", 2))
                    .withStrategy(Wait.forListeningPort())
            )

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) = registry.apply {
            add("spring.datasource.url", container::getJdbcUrl)
            add("spring.datasource.username", container::getUsername)
            add("spring.datasource.password", container::getPassword)
            add("spring.datasource.driver-class-name", container::getDriverClassName)
        }

        @JvmStatic
        @BeforeAll
        fun init() = container.start()

    }

}