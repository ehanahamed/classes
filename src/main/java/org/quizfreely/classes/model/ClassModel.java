package org.quizfreely.classes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Table(name = "classes.classes")
public class ClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private List<Teacher> teachers;
    private List<Student> students;
    private long courseId;

    public ClassEntity() {
        name = "Untitled Class";
    }
    public ClassEntity(long id) {
        name = "Untitled Class";
    }
    public ClassEntity(long id, String name) {
        this.name = name;
    }
    public ClassEntity(long id, String name, long courseId) {
        this.name = name;
        this.courseId = courseId;
    }
    public ClassEntity(
        long id, String name, long courseId, List<Teacher> teachers
    ) {
        this.name = name;
        this.courseId = courseId;
        this.teachers = teachers;
    }
    public ClassEntity(
        long id,
        String name,
        long courseId,
        List<Teacher> teachers,
        List<Student> students
    ) {
        this.name = name;
        this.courseId = courseId;
        this.teachers = teachers;
        this.students = students;
    }
}

