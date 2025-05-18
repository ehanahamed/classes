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
import org.quizfreely.classes.models.ClassUserSettings;

@Repository
public class ClassUserSettingsRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<ClassUserSettings> classUserSettingsRowMapper = new RowMapper<ClassUserSettings>() {
        public ClassUserSettings mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return new ClassUserSettings(
                resultSet.getString("color")
            );
        }
    };

    public ClassUserSettings getClassUserSettings(long classId, UUID authedUserId) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT color FROM classes.class_user_settings " +
                "WHERE class_id = ? and user_id = ?",
                new Object[] { classId, authedUserId },
                classUserSettingsRowMapper
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public ClassUserSettings updateOrInsertClassUserSettings(long classId, ClassUserSettings classUserSettings, UUID authedUserId) {
        try {
            return jdbcTemplate.queryForObject(
                "INSERT INTO classes.class_user_settings (class_id, user_id, color) " +
                "VALUES (?, ?, ?) " +
                "ON CONFLICT (class_id, user_id) DO UPDATE SET color = ? " +
                "RETURNING color",
                new Object[] {
                    classId,
                    authedUserId,
                    classUserSettings.getColor(),
                    classUserSettings.getColor()
                },
                classUserSettingsRowMapper
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}

