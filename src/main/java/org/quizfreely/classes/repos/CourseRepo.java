package org.quizfreely.classes.repos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.quizfreely.classes.models.Course;

@Repository
public class CourseRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Course> courseRowMapper = new RowMapper<Course>() {
        public Course mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return new Course(
                resultSet.getLong("id"),
                resultSet.getString("name")
            );
        }
    };

    public Course getCourseById(long id) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT id, name FROM classes.courses WHERE id = ?",
                new Object[] { id },
                courseRowMapper
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public Course createCourse(String name, UUID authedUserId) {
        Course newCourse = jdbcTemplate.queryForObject(
            "INSERT INTO classes.courses (name) VALUES (?) RETURNING id, name",
            new Object[] { name },
            courseRowMapper
        );
        if (
            jdbcTemplate.update(
                "INSERT INTO classes.course_authors (course_id, author_user_id) " +
                "VALUES (?, ?)",
                newCourse.getId(),
                authedUserId
            ) > 0
        ) {
            return newCourse;
        } else {
            return null;
        }
    }

    public Course updateCourse(long id, Course course, UUID authedUserId) {
        try {
            return jdbcTemplate.queryForObject(
                "UPDATE classes.courses SET name = ? " +
                "WHERE id = ? AND EXISTS (" +
                "    SELECT 1 FROM classes.course_authors ca " +
                "    WHERE ca.course_id = ? AND ca.author_user_id = ? " +
                ") " +
                "RETURNING id, name",
                new Object[] {
                    course.getName(),
                    id,
                    id,
                    authedUserId
                },
                courseRowMapper
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}

