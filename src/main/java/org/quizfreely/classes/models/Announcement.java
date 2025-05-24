package org.quizfreely.classes.models;
import java.util.UUID;
import java.time.OffsetDateTime;

public class Announcement {
    private long id;
    private UUID userId;
    private long classId;
    private String contentProseMirrorJson;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public Announcement(
        long id,
        UUID userId,
        long classId,
        String contentProseMirrorJson,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.classId = classId;
        this.contentProseMirrorJson = contentProseMirrorJson;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Announcement(
        UUID userId,
        long classId,
        String contentProseMirrorJson
    ) {
        this.userId = userId;
        this.classId = classId;
        this.contentProseMirrorJson = contentProseMirrorJson;
    }

    public long getId() {
        return id;
    }
    public UUID getUserId() {
        return userId;
    }
    public long getClassId() {
        return classId;
    }
    public String getContentProseMirrorJson() {
        return contentProseMirrorJson;
    }
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}

