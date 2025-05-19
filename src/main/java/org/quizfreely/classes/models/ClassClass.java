package org.quizfreely.classes.models;

public class ClassClass {
    private long id;
    private String name;
    private long courseId;
    private String color;
    
    public ClassClass() {}

    public ClassClass(
        String name
    ) {
        this.name = name;
    }
    public ClassClass(
        String name,
        long courseId
    ) {
        this.name = name;
        this.courseId = courseId;
    }
    public ClassClass(
        String name,
        long courseId,
        String color
    ) {
        this.name = name;
        this.courseId = courseId;
        this.color = color;
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
    public ClassClass(
        long id,
        String name,
        long courseId,
        String color
    ) {
        this.id = id;
        this.name = name;
        this.courseId = courseId;
        this.color = color;
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
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
}

