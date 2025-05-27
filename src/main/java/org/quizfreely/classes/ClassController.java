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
import org.quizfreely.classes.models.ClassClass;
import org.quizfreely.classes.models.Course;
import org.quizfreely.classes.models.User;
import org.quizfreely.classes.models.Announcement;
import org.quizfreely.classes.models.Assignment;
import org.quizfreely.classes.models.ClassUserSettings;
import org.quizfreely.classes.repos.ClassRepo;
import org.quizfreely.classes.repos.CourseRepo;
import org.quizfreely.classes.repos.UserRepo;
import org.quizfreely.classes.repos.ClassUserSettingsRepo;
import org.quizfreely.classes.repos.AnnouncementRepo;
import org.quizfreely.classes.repos.AssignmentRepo;

@Controller
public class ClassController {
    @Autowired
    ClassRepo classRepo;
    @Autowired
    CourseRepo courseRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    ClassUserSettingsRepo classUserSettingsRepo;
    @Autowired
    AnnouncementRepo announcementRepo;
    @Autowired
    AssignmentRepo assignmentRepo;

    @QueryMapping
    public ClassClass classById(@Argument long id) {
        return classRepo.getClassById(id);
    }

    @QueryMapping
    public List<ClassClass> classesAsTeacher(DataFetchingEnvironment dataFetchingEnv) {
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
    public List<ClassClass> classesAsStudent(DataFetchingEnvironment dataFetchingEnv) {
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
    public ClassClass createClass(@Argument String name, @Argument long courseId, DataFetchingEnvironment dataFetchingEnv) {
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
    public ClassClass updateClass(
        @Argument long id,
        @Argument String name,
        @Argument long courseId,
        @Argument String color,
        DataFetchingEnvironment dataFetchingEnv
    ) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
        if (authContext.isAuthed()) {
            return classRepo.updateClass(
                id,
                new ClassClass(name, courseId, color),
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
    public Course course(ClassClass classModel) {
        return courseRepo.getCourseById(classModel.getCourseId());
    }

    @SchemaMapping
    public List<User> students(ClassClass classModel) {
        return userRepo.getStudentsByClassId(classModel.getId());
    }

    @SchemaMapping
    public List<User> teachers(ClassClass classModel) {
        return userRepo.getTeachersByClassId(classModel.getId());
    }

    @SchemaMapping
    public ClassUserSettings userSettings(ClassClass classClass, DataFetchingEnvironment dataFetchingEnv) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
        if (authContext.isAuthed()) {
            return classUserSettingsRepo.getClassUserSettings(
                classClass.getId(),
                authContext.getAuthedUser().getId()
            );
        } else {
            return null;
        }
    }

    @SchemaMapping
    public List<Announcement> announcements(ClassClass classClass, DataFetchingEnvironment dataFetchingEnv) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
        if (authContext.isAuthed()) {
            return announcementRepo.getAnnouncementsByClassId(
                classClass.getId(),
                authContext.getAuthedUser().getId()
            );
        } else {
            return null;
        }
    }

    @SchemaMapping
    public List<Assignment> assignments(ClassClass classClass, DataFetchingEnvironment dataFetchingEnv) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
        if (authContext.isAuthed()) {
            return assignmentRepo.getAssignmentsByClassId(
                classClass.getId(),
                authContext.getAuthedUser().getId()
            );
        } else {
            return null;
        }
    }

    @SchemaMapping
    public List<Assignment> assignmentDrafts(ClassClass classClass, DataFetchingEnvironment dataFetchingEnv) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
        if (authContext.isAuthed()) {
            return assignmentRepo.getAssignmentDraftsByClassId(
                classClass.getId(),
                authContext.getAuthedUser().getId()
            );
        } else {
            return null;
        }
    }

    @SchemaMapping
    public String classCode(ClassClass classClass) {
        return classCodeRepo.getClassCodeByClassId(
            classClass.getId(),
        );
    }

    @MutationMapping
    public Long joinClass(@Argument String classCode, DataFetchingEnvironment dataFetchingEnv) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
        if (authContext.isAuthed()) {
            return classRepo.joinClassUsingClassCode(
                classCode,
                authContext.getAuthedUser().getId()
            );
        } else {
            return null;
        }
    }
}

