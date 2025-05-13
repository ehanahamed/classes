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
    private Course course;

    public ClassEntity() {
        name = "Untitled Class";
    }
    public ClassEntity(long id) {
        name = "Untitled Class";
    }
    public ClassEntity(long id, String name) {
        this.name = name;
    }
    public ClassEntity(long id, String name, Course course) {
        this.name = name;
        this.course = course;
    }
    public ClassEntity(
        long id, String name, Course course, List<Teacher> teachers
    ) {
        this.name = name;
        this.course = course;
        this.teachers = teachers;
    }
    public ClassEntity(
        long id,
        String name,
        Course course,
        List<Teacher> teachers,
        List<Student> students
    ) {
        this.name = name;
        this.course = course;
        this.teachers = teachers;
        this.students = students;
    }
}

