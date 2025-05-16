package org.quizfreely.classes.repos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import org.quizfreely.classes.models.ClassModel;

@Repository
public class ClassRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<ClassModel> classRowMapper = new RowMapper<ClassModel>() {
        public ClassModel mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return new ClassModel(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getLong("courseId")
            );
        }
    };

    public ClassModel getClassById(long id) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT id, name, course_id FROM classes.classes WHERE id = ?",
                new Object[] { id },
                classRowMapper
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<ClassModel> getClassesByStudentId(UUID studentUserId) {
        return jdbcTemplate.query(
            "SELECT c.id, c.name, c.course_id FROM classes.classes c " +
            "JOIN classes.classes_students cs ON cs.class_id = c.id " +
            "WHERE cs.student_user_id = ?",
            new Object[] { studentUserId },
            classRowMapper
        );
    }
    public List<ClassModel> getClassesByTeacherId(UUID teacherUserId) {
        return jdbcTemplate.query(
            "SELECT c.id, c.name, c.course_id FROM classes.classes c " +
            "JOIN classes.classes_teachers ct ON ct.class_id = c.id " +
            "WHERE ct.teacher_user_id = ?",
            teacherUserId,
            classRowMapper
        );
    }
    public ClassModel createClass(String name, long courseId) {

        return jdbcTemplate.queryForObject(
            "INSERT INTO classes (name, course_id) " + 
            "VALUES (?, ?) RETURNING id, name, course_id",
            new Object[] { name, courseId },
            classRowMapper
        );
    }
    public boolean addStudentToClassUsingAuthedId(
        UUID studentUserId, long classId, UUID authedUserId
    ) {
        return jdbcTemplate.update(
            "INSERT INTO classes_students (class_id, student_user_id) " + 
            "VALUES (?, ?) " +
            "WHERE EXISTS (" +
            "    SELECT 1 FROM classes_teachers " +
            "    WHERE teacher_user_id = ? AND class_id = ?" +
            ")",
            classId,
            studentUserId,
            authedUserId,
            classId
        ) > 0;
    }
    public boolean addTeacherToClassUsingAuthedId(
        UUID teacherUserId, long classId, UUID authedUserId
    ) {
        return jdbcTemplate.update(
            "INSERT INTO classes_teachers (class_id, teacher_user_id) " + 
            "VALUES (?, ?) " +
            "WHERE EXISTS (" +
            "    SELECT 1 FROM classes_teachers " +
            "    WHERE teacher_user_id = ? AND class_id = ?" +
            ")",
            classId,
            teacherUserId,
            authedUserId,
            classId
        ) > 0;
    }
}

