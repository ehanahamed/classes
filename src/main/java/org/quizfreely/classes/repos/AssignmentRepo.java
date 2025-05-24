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
import java.time.OffsetDateTime;
import org.quizfreely.classes.models.Assignment;

@Repository
public class AssignmentRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Assignment> assignmentRowMapper = new RowMapper<Assignment>() {
        public Assignment mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return new Assignment(
                resultSet.getLong("id"),
                resultSet.getLong("class_id"),
                resultSet.getObject(
                    "teacher_id",
                    UUID.class
                ),
                resultSet.getString("title"),
                resultSet.getString(
                    "description_prosemirror_json"
                ),
                resultSet.getShort("points"),
                resultSet.getObject(
                    "due_at",
                    OffsetDateTime.class
                ),
                resultSet.getObject(
                    "created_at",
                    OffsetDateTime.class
                ),
                resultSet.getObject(
                    "updated_at",
                    OffsetDateTime.class
                )
            );
        }
    };

    public Assignment getAssignmentById(long id) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT id, class_id, teacher_id, title, description_prosemirror_json, points, due_at, created_at, updated_at " +
                "FROM classes.assignments WHERE id = ?",
                new Object[] { id },
                assignmentRowMapper
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Assignment createAssignment(Assignment assignment, UUID authedUserId) {
        try {
            return jdbcTemplate.queryForObject(
                "INSERT INTO classes.assignments (class_id, teacher_id, title, description_prosemirror_json, points, due_at) " +
                "SELECT ?, ?, ?, ?::jsonb, ?, ? " +
                "WHERE EXISTS (" +
                "    SELECT 1 FROM classes.classes_teachers ct " +
                "    WHERE ct.class_id = ? AND ct.teacher_user_id = ? " +
                ") " +
                "RETURNING id, class_id, teacher_id, title, description_prosemirror_json, points, due_at, created_at, updated_at",
                new Object[] {
                    assignment.getClassId(),
                    authedUserId,
                    assignment.getTitle(),
                    assignment.getDescriptionProseMirrorJson(),
                    assignment.getPoints(),
                    assignment.getDueAt(),
                    assignment.getClassId(),
                    authedUserId
                },
                announcementRowMapper
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Announcement updateAnnouncement(long id, String contentProseMirrorJson, UUID authedUserId) {
        try {
            return jdbcTemplate.queryForObject(
                "UPDATE classes.announcements " +
                "SET content_prosemirror_json = ?::jsonb " +
                "    updated_at = now() " +
                "WHERE id = ? AND user_id = ? " +
                "RETURNING id, user_id, class_id, content_prosemirror_json, created_at, updated_at",
                new Object[] {
                    contentProseMirrorJson,
                    id,
                    authedUserId
                },
                announcementRowMapper
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Announcement> getAnnouncementsByClassId(long classId, UUID authedUserId) {
        return jdbcTemplate.query(
            "SELECT id, user_id, class_id, content_prosemirror_json, created_at, updated_at " +
            "FROM classes.announcements " +
            "WHERE class_id = ? AND (" +
            "    EXISTS (" +
            "        SELECT 1 FROM classes.classes_teachers ct " +
            "        WHERE ct.class_id = ? AND ct.teacher_user_id = ? " +
            "    ) OR EXISTS (" +
            "        SELECT 1 FROM classes.classes_students cs " +
            "        WHERE cs.class_id = ? AND cs.student_user_id = ? " +
            "    )" + 
            ")",
            new Object[] {
                classId,
                classId,
                authedUserId,
                classId,
                authedUserId
            },
            announcementRowMapper
        );
    }
}

