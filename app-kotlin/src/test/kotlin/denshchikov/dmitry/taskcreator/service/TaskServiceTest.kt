package denshchikov.dmitry.taskcreator.service

import denshchikov.dmitry.taskcreator.config.IntegrationTest
import denshchikov.dmitry.taskcreator.model.Task
import org.assertj.core.api.BDDAssertions.catchException
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.EmptyResultDataAccessException
import java.util.*

@SpringBootTest
internal class TaskServiceTest : IntegrationTest {

    @Autowired
    lateinit var taskService: TaskService


    @Test
    fun should_MarkTaskAsCompleted_When_TaskCreated() {
        // Given
        val taskId = UUID.randomUUID()
        val taskForCreate = Task(taskId, "Test title", "Test description")
        val taskForUpdate = Task(taskId, "Test title", "Test description", true)

        // When
        taskService.save(taskForCreate)
        taskService.update(taskForUpdate)
        val result = taskService.get(taskId)

        // Then
        then(result.isCompleted)
            .isTrue
    }

    @Test
    fun should_ThrowException_When_RetrievingTaskAfterDeletion() {
        // Given
        val taskId = UUID.randomUUID()
        val taskForCreate = Task(taskId, "Test title", "Test description")

        // When
        taskService.save(taskForCreate)
        taskService.delete(taskId)
        val exception = catchException { taskService.get(taskId) }

        // Then
        then(exception)
            .isInstanceOf(EmptyResultDataAccessException::class.java)
            .hasNoCause()
    }

}