package denshchikov.dmitry.taskcreator.service;

import denshchikov.dmitry.taskcreator.config.IntegrationTest;
import denshchikov.dmitry.taskcreator.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.catchException;
import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
public class TaskServiceTest implements IntegrationTest {

  @Autowired TaskService taskService;

  @Test
  void should_MarkTaskAsCompleted_When_TaskCreated() {
    // Given
    var taskId = UUID.randomUUID();
    var taskForCreate = new Task(taskId, "Test title", "Test description", false);
    var taskForUpdate = new Task(taskId, "Test title", "Test description", true);

    // When
    taskService.save(taskForCreate);
    taskService.update(taskForUpdate);
    var result = taskService.get(taskId);

    // Then
    then(result.isCompleted()).isTrue();
  }

  @Test
  void should_ThrowException_When_RetrievingTaskAfterDeletion() {
    // Given
    var taskId = UUID.randomUUID();
    var taskForCreate = new Task(taskId, "Test title", "Test description", false);

    // When
    taskService.save(taskForCreate);
    taskService.delete(taskId);
    var exception = catchException(() -> taskService.get(taskId));

    // Then
    then(exception).isInstanceOf(EmptyResultDataAccessException.class).hasNoCause();
  }
}
