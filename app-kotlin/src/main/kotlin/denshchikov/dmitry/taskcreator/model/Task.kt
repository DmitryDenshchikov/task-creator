package denshchikov.dmitry.taskcreator.model

import java.util.UUID

data class Task(
    val id: UUID,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false
)