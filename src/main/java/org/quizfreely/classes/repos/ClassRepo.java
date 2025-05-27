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
import org.quizfreely.classes.models.ClassClass;
import org.quizfreely.classes.services.ClassCodes;

@Repository
public class ClassRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<ClassClass> classRowMapper = new RowMapper<ClassClass>() {
        public ClassClass mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            ClassClass classClass = new ClassClass(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getLong("course_id")
            );
            classClass.setColor(
                resultSet.getString("color")
            );
            return classClass;
        }
    };

    public ClassClass getClassById(long id) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT id, name, course_id, color FROM classes.classes WHERE id = ?",
                new Object[] { id },
                classRowMapper
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public ClassClass updateClass(long id, ClassClass classModel, UUID authedUserId) {
        try {
            return jdbcTemplate.queryForObject(
                "UPDATE classes.classes SET name = ?, course_id = ?, color = ? " +
                "WHERE id = ? AND EXISTS (" +
                "    SELECT 1 FROM classes.classes_teachers ct " +
                "    WHERE ct.class_id = ? AND ct.teacher_user_id = ? " +
                ") " +
                "RETURNING id, name, course_id, color",
                new Object[] {
                    classModel.getName(),
                    classModel.getCourseId(),
                    classModel.getColor(),
                    id,
                    id,
                    authedUserId
                },
                classRowMapper
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<ClassClass> getClassesByStudentId(UUID studentUserId) {
        return jdbcTemplate.query(
            "SELECT c.id, c.name, c.course_id, color FROM classes.classes c " +
            "JOIN classes.classes_students cs ON cs.class_id = c.id " +
            "WHERE cs.student_user_id = ?",
            new Object[] { studentUserId },
            classRowMapper
        );
    }
    public List<ClassClass> getClassesByTeacherId(UUID teacherUserId) {
        return jdbcTemplate.query(
            "SELECT c.id, c.name, c.course_id, color FROM classes.classes c " +
            "JOIN classes.classes_teachers ct ON ct.class_id = c.id " +
            "WHERE ct.teacher_user_id = ?",
            new Object[] { teacherUserId },
            classRowMapper
        );
    }
    public ClassClass createClass(String name, long courseId, UUID authedUserId) {
        ClassClass newClass = jdbcTemplate.queryForObject(
            "INSERT INTO classes.classes (name, course_id) " + 
            "VALUES (?, ?) RETURNING id, name, course_id, color",
            new Object[] { name, courseId },
            classRowMapper
        );
        if (jdbcTemplate.update(
            "INSERT INTO classes.classes_teachers (class_id, teacher_user_id) " +
            "VALUES (?, ?)",
            newClass.getId(),
            authedUserId
        ) > 0) {
            return newClass;
        } else {
            return null;
        }
    }
    /* returns Long with joined classId */
    public Long joinClassUsingClassCode(
        String classCode, UUID authedUserId
    ) {
        try {
            return jdbcTemplate.queryForObject(
                "INSERT INTO classes.classes_students (class_id, student_user_id) " + 
                "SELECT class_id, ? FROM classes.class_codes " +
                "WHERE code = ? AND NOT EXISTS (" +
                "    SELECT 1 FROM classes.classes_students " +
                "    WHERE student_user_id = ? AND class_id = classes.class_codes.class_id " +
                ") AND NOT EXISTS (" +
                "    SELECT 1 FROM classes.classes_teachers " +
                "    WHERE teacher_user_id = ? AND class_id = classes.class_codes.class_id " +
                ") RETURNING class_id",
                Long.class,
                authedUserId,
                classCode,
                authedUserId,
                authedUserId
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    // public boolean addTeacherToClassUsingAuthedId(
    //     UUID teacherUserId, long classId, UUID authedUserId
    // ) {
    //     return jdbcTemplate.update(
    //         "INSERT INTO classes_teachers (class_id, teacher_user_id) " + 
    //         "VALUES (?, ?) " +
    //         "WHERE EXISTS (" +
    //         "    SELECT 1 FROM classes_teachers " +
    //         "    WHERE teacher_user_id = ? AND class_id = ?" +
    //         ")",
    //         classId,
    //         teacherUserId,
    //         authedUserId,
    //         classId
    //     ) > 0;
    // }
}

