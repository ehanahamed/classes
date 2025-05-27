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
import org.quizfreely.classes.repos.ClassCodeRepo;

@Controller
public class ClassCodeController {
    @Autowired
    ClassCodeRepo classCodeRepo;

    @MutationMapping
    public String generateClassCode(
        @Argument long classId,
        DataFetchingEnvironment dataFetchingEnv
    ) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
        if (authContext.isAuthed()) {
            UUID authedUserId = authContext.getAuthedUser().getId();
            return classCodeRepo.generateClassCode(
                classId,
                authedUserId
            );
        } else {
            return null;
        }
    }
}

