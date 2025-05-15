package org.quizfreely.classes.repos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import org.quizfreely.classes.model.ClassModel;

public class ClassRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    List<ClassModel> getClassesByStudentId(UUID authedUserId) {
        return jdbcTemplate.query(
            "SELECT id, name, courseId from classes.classes where ",
            (resultSet) -> new ClassModel(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getLong("courseId")
            )
        );
    }
    ClassModel getClassById(long id) {
        return jdbcTemplate.query(
            "SELECT id, name, courseId from classes.classes where id = ?",
            (resultSet) -> new ClassModel(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getLong("courseId")
            ),
            id
        )[0];
    }
}
