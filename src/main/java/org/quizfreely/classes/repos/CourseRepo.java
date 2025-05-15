package org.quizfreely.classes.repos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.quizfreely.classes.models.Course;

@Repository
public class CourseRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Course getCourseById(long id) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT id, name FROM classes.courses WHERE id = ?",
                new Object[] { id },
                (resultSet, rowNum) -> new Course(
                    resultSet.getLong("id"),
                    resultSet.getString("name")
                )
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}

