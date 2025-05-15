package org.quizfreely.classes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import graphql.schema.DataFetchingEnvironment;
import graphql.GraphQLContext;

import java.util.List;

import org.quizfreely.classes.auth.AuthContext;
import org.quizfreely.classes.models;
import org.quizfreely.classes.repos;

@Controller
public class ClassController {
    @Autowired
    ClassRepo classRepo;
    @Autowired
    CourseRepo courseRepo;

    @QueryMapping
    public ClassModel getClassById(@Argument long id) {
        return classRepo.getById(id);
    }

    @QueryMapping
    public List<ClassModel> getClassesAsTeacher(DataFetchingEnvironment dataFetchingEnv) {
        AuthContext authContext = dataFetchingEnv.getContext().get("authContext");
        if (authContext.isAuthed()) {
            return classRepo.getClassesByTeacherId(
                authContext.getAuthedUser().getId()
            );
        } else {
            return null;
        }
    }
    @QueryMapping
    public List<ClassModel> getClassesAsStudent(DataFetchingEnvironment dataFetchingEnv) {
        AuthContext authContext = dataFetchingEnv.getContext().get("authContext");
        if (authContext.isAuthed()) {
            return classRepo.getClassesByStudentId(
                authContext.getAuthedUser().getId()
            );
        } else {
            return null;
        }
    }

    @SchemaMapping
    public Course course(ClassModel classModel) {
        return courseRepo.getById(classEntity.courseId);
    }
}

