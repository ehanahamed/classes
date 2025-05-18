package org.quizfreely.classes.models;

public class ClassClass {
    private long id;
    private String name;
    private long courseId;
    
    public ClassClass() {}

    public ClassClass(
        String name,
        long courseId
    ) {
        this.name = name;
        this.courseId = courseId;
    }

    public ClassClass(
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

