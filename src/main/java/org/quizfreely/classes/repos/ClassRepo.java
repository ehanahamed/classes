package org.quizfreely.classes.repos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import org.quizfreely.classes.models.ClassModel;

@Repository
public class ClassRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    public List<ClassModel> getClassesByStudentId(UUID studentUserId) {
        return jdbcTemplate.query(
            "SELECT c.id, c.name, c.course_id FROM classes.classes c " +
            "JOIN classes.classes_students cs ON c.id = cs.class_id " +
            "WHERE cs.student_user_id = ?",
            (resultSet) -> new ClassModel(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getLong("course_id")
            ),
            studentUserId
        );
    }
    public List<ClassModel> getClassesByTeacherId(UUID teacherUserId) {
        return jdbcTemplate.query(
            "SELECT c.id, c.name, c.course_id FROM classes.classes c " +
            "JOIN classes.classes_teachers ct ON c.id = ct.class_id " +
            "WHERE ct.teacher_user_id = ?",
            (resultSet) -> new ClassModel(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getLong("course_id")
            ),
            teacherUserId
        );
    }
}

