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
import org.quizfreely.classes.models.User;
import org.quizfreely.classes.repos.AnnouncementRepo;
import org.quizfreely.classes.repos.UserRepo;

@Controller
public class AnnouncementController {
    @Autowired
    AnnouncementRepo announcementRepo;
    @Autowired
    UserRepo userRepo;

    @QueryMapping
    public Announcement announcementById(@Argument long id) {
        return announcementRepo.getAnnouncementById(id);
    }

    @MutationMapping
    public Announcement createAnnouncement(
        @Argument long classId,
        @Argument String contentProseMirrorJson,
        DataFetchingEnvironment dataFetchingEnv
    ) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
        if (authContext.isAuthed()) {
            UUID authedUserId = authContext.getAuthedUser().getId();
            return announcementRepo.createAnnouncement(
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
    public Announcement updateAnnouncement(
        @Argument long id,
        @Argument String contentProseMirrorJson,
        DataFetchingEnvironment dataFetchingEnv
    ) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
        if (authContext.isAuthed()) {
            return announcementRepo.updateAnnouncement(
                id,
                contentProseMirrorJson,
                authContext.getAuthedUser().getId()
            );
        } else {
            return null;
        }
    }

    @SchemaMapping
    public User user(Announcement announcement) {
        return userRepo.getUserById(announcement.getUserId());
    }
}

