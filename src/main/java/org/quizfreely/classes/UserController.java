package org.quizfreely.classes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import graphql.schema.DataFetchingEnvironment;

import java.util.UUID;
import java.util.List;

import org.quizfreely.classes.models.User;
import org.quizfreely.classes.repos.UserRepo;
import org.quizfreely.classes.auth.AuthContext;
import org.quizfreely.classes.auth.AuthedUser;

@Controller
public class UserController {
    @Autowired
    private UserRepo userRepo;

    @QueryMapping
    public User getUserById(UUID id) {
        return userRepo.getUserById(id);
    }
}

