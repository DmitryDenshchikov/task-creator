package denshchikov.dmitry.taskcreator.config

import jakarta.annotation.PostConstruct
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class FlywayConfig(val dataSource: DataSource) {

    @PostConstruct
    fun init(): MigrateResult = Flyway.configure()
        .dataSource(dataSource)
        .load()
        .migrate()

}