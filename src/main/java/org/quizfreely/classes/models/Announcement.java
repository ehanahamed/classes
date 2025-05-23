package org.quizfreely.classes.models;
import java.util.UUID;
import java.util.Map;

public class Announcement {
    private long id;
    private UUID userId;
    private long classId;
    private Map<String, Object> contentJson;

    public Announcement(
        long id,
        UUID userId,
        long classId,
        Map<String, Object> contentJson
    ) {
        this.id = id;
        this.userId = userId;
        this.classId = classId;
        this.contentJson = contentJson;
    }
    public Announcement(
        UUID userId,
        long classId,
        Map<String, Object> contentJson
    ) {
        this.userId = userId;
        this.classId = classId;
        this.contentJson = contentJson;
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
    public Map<String, Object> getContentJson() {
        return contentJson;
    }
}

