package denshchikov.dmitry.taskcreator.service;

import denshchikov.dmitry.taskcreator.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TaskService {

  private final JdbcTemplate jdbcTemplate;
  private final RowMapper<Task> taskRowMapper =
      (rs, rowNum) ->
          new Task(
              UUID.fromString(rs.getString("id")),
              rs.getString("title"),
              rs.getString("description"),
              rs.getBoolean("is_completed"));

  public TaskService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Transactional
  public int save(Task task) {
    return jdbcTemplate.update(
        "INSERT INTO task VALUES (?, ?, ?, ?);",
        task.id(),
        task.title(),
        task.description(),
        task.isCompleted());
  }

  @Transactional
  public int update(Task task) {
    return jdbcTemplate.update(
        "UPDATE task SET (title, description, is_completed) = (?, ?, ?) WHERE id = ?;",
        task.title(),
        task.description(),
        task.isCompleted(),
        task.id());
  }

  public Task get(UUID id) {
    return jdbcTemplate.queryForObject("SELECT * FROM task WHERE id = ?;", taskRowMapper, id);
  }

  @Transactional
  public int delete(UUID id) {
    return jdbcTemplate.update("DELETE FROM task WHERE id = ?;", id);
  }
}
