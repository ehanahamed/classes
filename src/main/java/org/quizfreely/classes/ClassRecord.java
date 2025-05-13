package org.quizfreely.classes;
import java.sql.*;
import java.util.List;

public record ClassRecord(long id, Course course, List<User> teachers, List<User> students) {
    
}

