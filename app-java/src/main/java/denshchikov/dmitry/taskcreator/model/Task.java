package denshchikov.dmitry.taskcreator.model;

import java.util.UUID;

public record Task(UUID id, String title, String description, Boolean isCompleted) {}
