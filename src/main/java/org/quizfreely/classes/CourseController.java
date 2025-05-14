package org.quizfreely.classes;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.quizfreely.classes.repo.CourseRepo;
import org.quizfreely.classes.model.Course;

@Controller
public class CourseController {
    @QueryMapping
    public Course getCourseById(@Argument long id) {
        return ClassRepo.getById(id);
    }
}
