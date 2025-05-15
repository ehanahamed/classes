package org.quizfreely.classes.repos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.quizfreely.classes.models.Course;

public class CourseRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Course getCourseById(long id) {
        return jdbcTemplate.queryForObject(
            "SELECT id, name FROM classes.courses WHERE id = ?",
            new Object[] { id },
            (resultSet, rowNum) -> new Course(
                resultSet.getLong("id"),
                resultSet.getString("name")
            )
        );
    }
}

