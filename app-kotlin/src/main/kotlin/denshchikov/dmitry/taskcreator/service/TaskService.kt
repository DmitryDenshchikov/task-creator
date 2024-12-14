package denshchikov.dmitry.taskcreator.service

import denshchikov.dmitry.taskcreator.model.Task
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet
import java.util.UUID

@Service
class TaskService(val template: JdbcTemplate) {

    @Transactional
    fun save(task: Task) = task.apply {
        template.update("INSERT INTO task VALUES (?, ?, ?, ?);", id, title, description, isCompleted)
    }

    @Transactional
    fun update(task: Task) = task.apply {
        template.update(
            "UPDATE task SET (title, description, is_completed) = (?, ?, ?) WHERE id = ?;",
            title, description, isCompleted, id
        )
    }

    fun get(id: UUID): Task {
        val rm = RowMapper { rs: ResultSet, _: Int ->
            Task(
                UUID.fromString(rs.getString("id")),
                rs.getString("title"),
                rs.getString("description"),
                rs.getBoolean("is_completed")
            )
        }

        return template.queryForObject("SELECT * FROM task WHERE id = ?;", rm, id)
            ?: throw NoSuchElementException("Task $id not found")
    }

    @Transactional
    fun delete(id: UUID) = template.update("DELETE FROM task WHERE id = ?;", id)

}