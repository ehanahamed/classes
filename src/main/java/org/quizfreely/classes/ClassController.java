package org.quizfreely.classes;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ClassController {
    @QueryMapping
    public ClassEntity classById(@Argument long id) {
        return classRepo.findById(id);
    }

    @SchemaMapping
    public 
}

