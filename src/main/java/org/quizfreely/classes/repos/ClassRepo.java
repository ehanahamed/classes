package org.quizfreely.classes.repos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;
import org.quizfreely.classes.models.ClassModel;

@Repository
public class ClassRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ClassModel getClassById(long id) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT id, name, course_id FROM classes.classes WHERE id = ?",
                new Object[] { id },
                (resultSet, rowNum) -> new ClassModel(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getLong("courseId")
                )
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<ClassModel> getClassesByStudentId(UUID studentUserId) {
        return jdbcTemplate.query(
            "SELECT c.id, c.name, c.course_id FROM classes.classes c " +
            "JOIN classes.classes_students cs ON c.id = cs.class_id " +
            "WHERE cs.student_user_id = ?",
            (resultSet, rowNum) -> new ClassModel(
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
            (resultSet, rowNum) -> new ClassModel(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getLong("course_id")
            ),
            teacherUserId
        );
    }
}

