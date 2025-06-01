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
                assignmentRowMapper
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Assignment updateAssignment(long id, Assignment assignment, UUID authedUserId) {
        try {
            return jdbcTemplate.queryForObject(
                "UPDATE classes.assignments " +
                "SET title = ?, " +
                "    description_prosemirror_json = ?::jsonb, " +
                "    points = ?, " +
                "    due_at = ?, " +
                "    updated_at = now() " +
                "WHERE id = ? AND EXISTS (" +
                "    SELECT 1 FROM classes.classes_teachers ct " +
                "    WHERE ct.class_id = ? AND ct.teacher_user_id = ? " +
                ") " +
                "RETURNING id, class_id, teacher_id, title, description_prosemirror_json, points, due_at, created_at, updated_at",
                new Object[] {
                    assignment.getTitle(),
                    assignment.getDescriptionProseMirrorJson(),
                    assignment.getPoints(),
                    assignment.getDueAt(),
                    id,
                    assignment.getClassId(),
                    authedUserId
                },
                assignmentRowMapper
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Assignment> getAssignmentsByClassId(long classId, UUID authedUserId) {
        return jdbcTemplate.query(
            """
            SELECT id, class_id, teacher_id, title, description_prosemirror_json, points, due_at, created_at, updated_at
            FROM classes.assignments
            WHERE class_id = ? AND (
                EXISTS (
                    SELECT 1 FROM classes.classes_teachers ct
                    WHERE ct.class_id = ? AND ct.teacher_user_id = ?
                ) OR EXISTS (
                    SELECT 1 FROM classes.classes_students cs
                    WHERE cs.class_id = ? AND cs.student_user_id = ?
                )
            )
            ORDER BY updated_at DESC
            """,
            new Object[] {
                classId,
                classId,
                authedUserId,
                classId,
                authedUserId
            },
            assignmentRowMapper
        );
    }

    public Assignment getAssignmentDraftById(long id, UUID authedUserId) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT id, class_id, teacher_id, title, description_prosemirror_json, points, due_at, created_at, updated_at " +
                "FROM classes.assignment_drafts WHERE id = ? AND teacher_id = ?",
                new Object[] { id, authedUserId },
                assignmentRowMapper
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Assignment createAssignmentDraft(Assignment assignment, UUID authedUserId) {
        try {
            return jdbcTemplate.queryForObject(
                "INSERT INTO classes.assignment_drafts (class_id, teacher_id, title, description_prosemirror_json, points, due_at) " +
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
                assignmentRowMapper
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Assignment updateAssignmentDraft(long id, Assignment assignment, UUID authedUserId) {
        try {
            return jdbcTemplate.queryForObject(
                "UPDATE classes.assignment_drafts " +
                "SET title = ?, " +
                "    description_prosemirror_json = ?::jsonb, " +
                "    points = ?, " +
                "    due_at = ?, " +
                "    updated_at = now() " +
                "WHERE id = ? AND teacher_id = ? " +
                "RETURNING id, class_id, teacher_id, title, description_prosemirror_json, points, due_at, created_at, updated_at",
                new Object[] {
                    assignment.getTitle(),
                    assignment.getDescriptionProseMirrorJson(),
                    assignment.getPoints(),
                    assignment.getDueAt(),
                    id,
                    authedUserId
                },
                assignmentRowMapper
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Assignment> getAssignmentDraftsByClassId(long classId, UUID authedUserId) {
        return jdbcTemplate.query(
            """
            SELECT id, class_id, teacher_id, title, description_prosemirror_json, points, due_at, created_at, updated_at
            FROM classes.assignment_drafts
            WHERE class_id = ? AND teacher_id = ?
            ORDER BY updated_at DESC
            """,
            new Object[] {
                classId,
                authedUserId
            },
            assignmentRowMapper
        );
    }

    public boolean deleteAssignment(long id, UUID authedUserId) {
        return jdbcTemplate.update(
            """
            DELETE FROM classes.assignments a
            WHERE a.id = ? AND EXISTS (
                SELECT 1 FROM classes.classes_teachers ct
                WHERE ct.class_id = a.class_id AND
                ct.teacher_user_id = ?
            )
            """,
            id,
            authedUserId
        ) > 0;
    }

    public boolean deleteAssignmentDraft(long id, UUID authedUserId) {
        return jdbcTemplate.update(
            """
            DELETE FROM classes.assignment_drafts a
            WHERE a.id = ? AND EXISTS (
                SELECT 1 FROM classes.classes_teachers ct
                WHERE ct.class_id = a.class_id AND
                ct.teacher_user_id = ?
            )
            """,
            id,
            authedUserId
        ) > 0;
    }

    public List<Assignment> getAllAssignmentsAsStudentWithAuthedUserId(UUID authedUserId) {
        return jdbcTemplate.query(
            """
            SELECT a.id, a.class_id, a.teacher_id, a.title, a.due_at, a.created_at, a.updated_at
            FROM classes.assignments a JOIN classes.classes_students cs ON a.class_id = cs.class_id
            WHERE cs.user_id = ?
            """,
            assignmentRowMapper,
            authedUserId
        );
    }
}

