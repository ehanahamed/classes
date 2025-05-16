package org.quizfreely.classes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.quizfreely.classes.repos.CourseRepo;
import org.quizfreely.classes.models.Course;

@Controller
public class CourseController {
    @Autowired
    private CourseRepo courseRepo;

    @QueryMapping
    public Course courseById(@Argument long id) {
        return courseRepo.getCourseById(id);
    }
}

