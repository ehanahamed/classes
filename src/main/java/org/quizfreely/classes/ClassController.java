package org.quizfreely.classes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import graphql.schema.DataFetchingEnvironment;

import java.util.List;

import org.quizfreely.classes.auth.AuthContext;
import org.quizfreely.classes.models.ClassModel;
import org.quizfreely.classes.models.Course;
import org.quizfreely.classes.repos.ClassRepo;
import org.quizfreely.classes.repos.CourseRepo;

@Controller
public class ClassController {
    @Autowired
    ClassRepo classRepo;
    @Autowired
    CourseRepo courseRepo;

    @QueryMapping
    public ClassModel classById(@Argument long id) {
        return classRepo.getClassById(id);
    }

    @QueryMapping
    public List<ClassModel> classesAsTeacher(DataFetchingEnvironment dataFetchingEnv) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
        if (authContext.isAuthed()) {
            return classRepo.getClassesByTeacherId(
                authContext.getAuthedUser().getId()
            );
        } else {
            return null;
        }
    }
    @QueryMapping
    public List<ClassModel> classesAsStudent(DataFetchingEnvironment dataFetchingEnv) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
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
        return courseRepo.getCourseById(classModel.getCourseId());
    }
}

