package org.quizfreely.classes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import graphql.schema.DataFetchingEnvironment;

import java.util.List;

import org.quizfreely.classes.auth.AuthRepo;
import org.quizfreely.classes.auth.AuthContext;
import org.quizfreely.classes.auth.AuthedUser;

@Controller
public class AuthController {
    @Autowired
    AuthRepo authRepo;

    @QueryMapping
    public AuthedUser getAuthedUser(DataFetchingEnvironment dataFetchingEnv) {
        AuthContext authContext = dataFetchingEnv.getGraphQlContext().get("authContext");
        if (authContext.isAuthed()) {
            return authContext.getAuthedUser();
        } else {
            return null;
        }
    }
}

