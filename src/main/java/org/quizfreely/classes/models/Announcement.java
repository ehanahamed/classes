package org.quizfreely.classes.models;
import java.util.UUID;

public class Announcement {
    private long id;
    private UUID userId;
    private long classId;
    private String contentProseMirrorJson;

    public Announcement(
        long id,
        UUID userId,
        long classId,
        String contentProseMirrorJson
    ) {
        this.id = id;
        this.userId = userId;
        this.classId = classId;
        this.contentProseMirrorJson = contentProseMirrorJson;
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
    public String getContentJson() {
        return contentProseMirrorJson;
    }
}

