package org.quizfreely.classes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Controller;
import org.quizfreely.classes.repos.CourseRepo;
import org.quizfreely.classes.models.Course;
import org.quizfreely.classes.auth.AuthContext;

@Controller
public class CourseController {
    @Autowired
    private CourseRepo courseRepo;

    @QueryMapping
    public Course courseById(@Argument long id) {
        return courseRepo.getCourseById(id);
    }

    @MutationMapping
    public Course createCourse(@Argument String name, DataFetchingEnvironment dataFetchingEnv) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
        if (authContext.isAuthed()) {
            return courseRepo.createCourse(
                name,
                authContext.getAuthedUser().getId()
            );
        } else {
            return null;
        }
    }
    
    @MutationMapping
    public Course updateCourse(@Argument long id, @Argument String name, DataFetchingEnvironment dataFetchingEnv) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
        if (authContext.isAuthed()) {
            return courseRepo.updateCourse(
                id,
                new Course(name),
                authContext.getAuthedUser().getId()
            );
        } else {
            return null;
        }
    }
}

