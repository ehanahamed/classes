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
import java.time.OffsetDateTime;

import org.quizfreely.classes.auth.AuthContext;
import org.quizfreely.classes.models.Assignment;
import org.quizfreely.classes.models.User;
import org.quizfreely.classes.repos.AssignmentRepo;
import org.quizfreely.classes.repos.UserRepo;

@Controller
public class AssignmentController {
    @Autowired
    AssignmentRepo assignmentRepo;
    @Autowired
    UserRepo userRepo;

    @QueryMapping
    public Assignment assignmentById(@Argument long id) {
        return assignmentRepo.getAssignmentById(id);
    }

    @MutationMapping
    public Assignment createAssignment(
        @Argument long classId,
        @Argument String title,
        @Argument String descriptionProseMirrorJson,
        @Argument short points,
        @Argument OffsetDateTime dueAt,
        DataFetchingEnvironment dataFetchingEnv
    ) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
        if (authContext.isAuthed()) {
            UUID authedUserId = authContext.getAuthedUser().getId();
            return assignmentRepo.createAssignment(
                new Assignment(
                    classId,
                    authedUserId,
                    title,
                    descriptionProseMirrorJson,
                    points,
                    dueAt
                ),
                authedUserId
            );
        } else {
            return null;
        }
    }

    @MutationMapping
    public Assignment updateAssignment(
        @Argument long id,
        @Argument long classId,
        @Argument String title,
        @Argument String descriptionProseMirrorJson,
        @Argument short points,
        @Argument OffsetDateTime dueAt,
        DataFetchingEnvironment dataFetchingEnv
    ) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
        if (authContext.isAuthed()) {
            UUID authedUserId = authContext.getAuthedUser().getId();
            return assignmentRepo.updateAssignment(
                id,
                new Assignment(
                    classId,
                    authedUserId,
                    title,
                    descriptionProseMirrorJson,
                    points,
                    dueAt
                ),
                authedUserId
            );
        } else {
            return null;
        }
    }

    @SchemaMapping
    public User teacher(Assignment assignment) {
        return userRepo.getUserById(assignment.getTeacherId());
    }
}

