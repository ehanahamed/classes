package org.quizfreely.classes.repos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import org.quizfreely.classes.services.ClassCodes;

@Repository
public class ClassCodeRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<String> classCodeRowMapper = new RowMapper<String>() {
        public String mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return resultSet.getString("code");
        }
    };

    public String generateClassCode(long classId, UUID authedUserId) {
        int maxAttempts = 5;
        int codeLength = 6;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            String code = ClassCodes.generateCode(codeLength);

            try {
                int rowsAffected = jdbcTemplate.update(
                    "INSERT INTO classes.class_codes (code, class_id) " +
                    "SELECT ?, ? WHERE EXISTS (" +
                    "    SELECT 1 FROM classes.classes_teachers " +
                    "    WHERE class_id = ? AND teacher_user_id = ?" +
                    ")",
                    code,
                    classId,
                    classId,
                    authedUserId
                );

                if (rowsAffected > 0) {
                    return code;
                } else {
                    return null; /* authedUser is not a teacher in this class */
                }

            } catch (DuplicateKeyException e) {
                /* this is inside a for loop, so it will retry if the code already exists
                and if it doesn't already exist, then the return statement will end the loop */
            }
        }
        // throw new RuntimeException("Failed to generate a unique code after " + maxAttempts + " attempts.");

        /* if it didnt return anything in the loop, then it means we reached max tries */
        return null; /* notice this is outside the loop */
    }

    public String getClassCodeByClassId(long classId) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT code FROM classes.class_codes WHERE class_id = ?",
                new Object[] { classId },
                classCodeRowMapper
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}

