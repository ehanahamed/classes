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
import org.quizfreely.classes.models.Announcement;
import org.quizfreely.classes.repos.AnnouncementRepo;

@Controller
public class ClassController {
    @Autowired
    AnnouncementRepo announcementRepo;

    @QueryMapping
    public Announcement announcementById(@Argument long id) {
        return announcementRepo.getAnnouncementById(id);
    }

    @QueryMapping
    public List<Announcement> announcementsByClassId(@Argument long classId, DataFetchingEnvironment dataFetchingEnv) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
        if (authContext.isAuthed()) {
            return announcementRepo.getAnnouncementsByClassId(
                classId,
                authContext.getAuthedUser().getId()
            );
        } else {
            return null;
        }
    }

    @MutationMapping
    public Announcement createAnnouncement(@Argument long classId, @Argument Map<String, Object> contentProseMirrorJson, DataFetchingEnvironment dataFetchingEnv) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
        if (authContext.isAuthed()) {
            UUID authedUserId = authContext.getAuthedUser().getId()
            return announcementRepo.createClass(
                new Announcement(
                    authedUserId,
                    classId,
                    contentProseMirrorJson
                ),
                authedUserId
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
}

