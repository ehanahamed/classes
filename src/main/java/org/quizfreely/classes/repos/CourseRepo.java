package org.quizfreely.classes.repos;

import org.springframework.jdbc.core.JdbcTemplate;
import org.quizfreely.classes.models.Course;

public class CourseRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Course getCourseById(long id) {
        jdbcTemplate.queryForObject(
            "SELECT id, name FROM classes.courses WHERE id = ?",
            Object[] { id },
            (resultSet) -> new Course(
                resultSet.getLong("id"),
                resultSet.getString("name")
            )
        )
    }
}

