package org.quizfreely.classes.models;
import java.util.UUID;

public class Announcement {
    private long id;
    private UUID userId;
    private long classId;
    private String contentJson;

    public Announcement(
        long id,
        UUID userId,
        long classId,
        String contentJson
    ) {
        this.id = id;
        this.userId = userId;
        this.classId = classId;
        this.contentJson = contentJson;
    }
    public Announcement(
        UUID userId,
        long classId,
        String contentJson
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
    public String getContentJson() {
        return contentJson;
    }
}

