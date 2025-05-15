package org.quizfreely.classes.models;

public class ClassModel {
    private long id;
    private String name;
    private long courseId;
    
    public ClassModel() {}

    public ClassModel(
        String name,
        long courseId
    ) {
        this.name = name;
        this.courseId = courseId;
    }

    public ClassModel(
        long id,
        String name,
        long courseId
    ) {
        this.id = id;
        this.name = name;
        this.courseId = courseId;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public long getCourseId() {
        return courseId;
    }
    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }
}

