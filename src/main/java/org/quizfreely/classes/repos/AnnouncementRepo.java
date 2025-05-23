package org.quizfreely.classes.repos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.json.JSONObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import org.quizfreely.classes.models.Announcement;

@Repository
public class AnnouncementRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Announcement> announcementRowMapper = new RowMapper<Announcement>() {
        public Announcement mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return new Announcement(
                resultSet.getLong("id"),
                (UUID) resultSet.getObject(
                    "user_id"
                ),
                resultSet.getLong("class_id"),
                (JSONObject) resultSet.getObject(
                    "content_prosemirror_json"
                )
            );
        }
    };

    public Announcement getAnnouncementById(long id) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT id, user_id, class_id, content_prosemirror_json FROM classes.announcements WHERE id = ?",
                new Object[] { id },
                announcementRowMapper
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Announcement createAnnouncement(Announcement announcement, UUID authedUserId) {
        try {
            return jdbcTemplate.queryForObject(
                "INSERT INTO classes.announcements (user_id, class_id, content_prosemirror_json) " +
                "VALUES (?, ?, ?) " +
                "WHERE (" +
                "    EXISTS (" +
                "        SELECT 1 FROM classes.classes_teachers ct " +
                "        WHERE ct.class_id = ? AND ct.teacher_user_id = ? " +
                "    ) OR EXISTS (" +
                "        SELECT 1 FROM classes.classes_students cs " +
                "        WHERE cs.class_id = ? AND cs.student_user_id = ? " +
                "    )" + 
                ")" +
                "RETURNING id, user_id, class_id, content_prosemirror_json",
                new Object[] {
                    authedUserId,
                    announcement.getClassId(),
                    announcement.getContentJson(),
                    announcement.getClassId(),
                    authedUserId,
                    announcement.getClassId(),
                    authedUserId
                },
                announcementRowMapper
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Announcement updateAnnouncement(long id, Announcement announcement, UUID authedUserId) {
        try {
            return jdbcTemplate.queryForObject(
                "UPDATE classes.announcements SET content_prosemirror_json = ? " +
                "WHERE id = ? AND user_id = ? " +
                "RETURNING id, user_id, class_id, content_prosemirror_json",
                new Object[] {
                    announcement.getContentJson(),
                    id,
                    authedUserId
                },
                announcementRowMapper
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Announcement> getAnnouncementsByClassId(long classId, authedUserId) {
        return jdbcTemplate.query(
            "SELECT id, user_id, class_id, content_prosemirror_json " +
            "FROM classes.announcements " +
            "WHERE class_id = ? AND (" +
            "    EXISTS (" +
            "        SELECT 1 FROM classes.classes_teachers ct " +
            "        WHERE ct.class_id = ? AND ct.teacher_user_id = ? " +
            "    ) OR EXISTS (" +
            "        SELECT 1 FROM classes.classes_students cs " +
            "        WHERE cs.class_id = ? AND cs.student_user_id = ? " +
            "    )" + 
            ")" +
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

