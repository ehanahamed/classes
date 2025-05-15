package org.quizfreely.classes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.quizfreely.classes.model;
import org.quizfreely.classes.repo;

@Controller
public class ClassController {
    @Autowired
    ClassRepo classRepo;

    @QueryMapping
    public ClassModel getClassById(@Argument long id) {
        return ClassRepo.getById(id);
    }

    @SchemaMapping
    public Course course(ClassEntity classEntity) {
        return CourseRepo.getById(classEntity.courseId);
    }
}
