package org.quizfreely.classes;

import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;
import graphql.schema.idl.RuntimeWiring;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLConfig {

    @Bean
    public GraphQLScalarType jsonScalar() {
        return ExtendedScalars.Json;
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer(GraphQLScalarType jsonScalar) {
        return wiringBuilder -> wiringBuilder.scalar(jsonScalar);
    }
}

