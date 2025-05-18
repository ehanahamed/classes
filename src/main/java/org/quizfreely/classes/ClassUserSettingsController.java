package org.quizfreely.classes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import graphql.schema.DataFetchingEnvironment;

import java.util.UUID;

import org.quizfreely.classes.auth.AuthContext;
import org.quizfreely.classes.models.ClassUserSettings;
import org.quizfreely.classes.repos.ClassUserSettingsRepo;

@Controller
public class ClassUserSettingsController {
    @Autowired
    ClassUserSettingsRepo classUserSettingsRepo;

    @QueryMapping
    public ClassUserSettings classUserSettings(@Argument long classId, DataFetchingEnvironment dataFetchingEnv) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
        if (authContext.isAuthed()) {
            return classUserSettingsRepo.getClassUserSettings(
                classId,
                authContext.getAuthedUser().getId()
            );
        } else {
            return null;
        }
    }

    @MutationMapping
    public ClassUserSettings updateClassUserSettings(@Argument long classId, @Argument String color, DataFetchingEnvironment dataFetchingEnv) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
        if (authContext.isAuthed()) {
            return classUserSettingsRepo.updateOrInsertClassUserSettings(
                classId,
                new ClassUserSettings(color),
                authContext.getAuthedUser().getId()
            );
        } else {
            return null;
        }
    }
}

