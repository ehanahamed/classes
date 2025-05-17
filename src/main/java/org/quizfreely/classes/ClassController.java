package org.quizfreely.classes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import graphql.schema.DataFetchingEnvironment;

import java.util.List;
import java.util.UUID;

import org.quizfreely.classes.auth.AuthContext;
import org.quizfreely.classes.models.ClassModel;
import org.quizfreely.classes.models.Course;
import org.quizfreely.classes.models.User;
import org.quizfreely.classes.repos.ClassRepo;
import org.quizfreely.classes.repos.CourseRepo;
import org.quizfreely.classes.repos.UserRepo;

@Controller
public class ClassController {
    @Autowired
    ClassRepo classRepo;
    @Autowired
    CourseRepo courseRepo;
    @Autowired
    UserRepo userRepo;

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

    @MutationMapping
    public boolean addStudentToClass(@Argument UUID studentUserId, @Argument long classId, DataFetchingEnvironment dataFetchingEnv) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
        if (authContext.isAuthed()) {
            return classRepo.addStudentToClassUsingAuthedId(
                studentUserId,
                classId,
                authContext.getAuthedUser().getId()
            );
        } else {
            return false;
        }
    }

    @MutationMapping
    public ClassModel createClass(@Argument String name, @Argument long courseId, DataFetchingEnvironment dataFetchingEnv) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
        if (authContext.isAuthed()) {
            return classRepo.createClass(
                name,
                courseId,
                authContext.getAuthedUser().getId()
            );
        } else {
            return null;
        }
    }

    @MutationMapping
    public ClassModel updateClass(@Argument long id, @Argument String name, @Argument long courseId, DataFetchingEnvironment dataFetchingEnv) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
        if (authContext.isAuthed()) {
            return classRepo.updateClass(
                name,
                new ClassModel(name, courseId)
                authContext.getAuthedUser().getId()
            );
        } else {
            return null;
        }
    }

    @MutationMapping
    public boolean addTeacherToClass(@Argument UUID teacherUserId, @Argument long classId, DataFetchingEnvironment dataFetchingEnv) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
        if (authContext.isAuthed()) {
            return classRepo.addTeacherToClassUsingAuthedId(
                teacherUserId,
                classId,
                authContext.getAuthedUser().getId()
            );
        } else {
            return false;
        }
    }

    @SchemaMapping
    public Course course(ClassModel classModel) {
        return courseRepo.getCourseById(classModel.getCourseId());
    }

    @SchemaMapping
    public List<User> students(ClassModel classModel) {
        return userRepo.getStudentsByClassId(classModel.getId());
    }

    @SchemaMapping
    public List<User> teachers(ClassModel classModel) {
        return userRepo.getTeachersByClassId(classModel.getId());
    }
}

