package org.quizfreely.classes.models;
import java.util.UUID;
import java.time.OffsetDateTime;

public class Assignment {
    private long id;
    private UUID teacherId;
    private long classId;
    private String title;
    private String descriptionProseMirrorJson;
    private short points;
    private OffsetDateTime dueAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public Assignment(
        long id,
        long classId,
        UUID teacherId,
        String title,
        String descriptionProseMirrorJson,
        short points,
        OffsetDateTime dueAt,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
    ) {
        this.id = id;
        this.classId = classId;
        this.teacherId = teacherId;
        this.title = title;
        this.descriptionProseMirrorJson = descriptionProseMirrorJson;
        this.points = points;
        this.dueAt = dueAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Assignment(
        long classId,
        UUID teacherId,
        String title,
        String descriptionProseMirrorJson,
        short points,
        OffsetDateTime dueAt
    ) {
        this.classId = classId;
        this.teacherId = teacherId;
        this.title = title;
        this.descriptionProseMirrorJson = descriptionProseMirrorJson;
        this.points = points;
        this.dueAt = dueAt;
    }

    public long getId() {
        return id;
    }
    public long getClassId() {
        return classId;
    }
    public UUID getTeacherId() {
        return teacherId;
    }
    public UUID getTitle() {
        return title;
    }
    public String getDescriptionProseMirrorJson() {
        return descriptionProseMirrorJson;
    }
    public short getPoints() {
        return points;
    }
    public OffsetDateTime getDueAt() {
        return dueAt;
    }
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}

