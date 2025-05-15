package org.quizfreely.classes.repos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import org.quizfreely.classes.models.ClassModel;

@Repository
public class ClassRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ClassModel> getClassesByStudentId(UUID authedUserId) {
        return jdbcTemplate.query(
            "SELECT c.id, c.name, c.course_id FROM classes.classes c " +
            "JOIN classes.classes_students cs ON c.id = cs.class_id " +
            "WHERE cs.student_id = ?",
            (resultSet) -> new ClassModel(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getLong("course_id")
            ),
            authedUserId
        );
    }
    public ClassModel getClassById(long id) {
        return jdbcTemplate.query(
            "SELECT id, name, course_id FROM classes.classes WHERE id = ?",
            (resultSet) -> new ClassModel(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getLong("courseId")
            ),
            id
        )[0];
    }
}

