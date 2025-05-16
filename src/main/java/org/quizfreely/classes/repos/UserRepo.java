package org.quizfreely.classes.repos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.List;
import org.quizfreely.classes.models.User;

@Repository
public class UserRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User getUserById(UUID id) {
        return jdbcTemplate.queryForObject(
            "SELECT id, display_name, username, oauth_google_email " +
            "FROM public.profiles WHERE id = ?",
            new Object[] { id },
            new RowMapper<User>() {
                public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                    User user = new User();
                    user.setId((UUID) resultSet.getObject("id"));
                    user.setDisplayName(resultSet.getString("display_name"));
                    user.setUsername(resultSet.getString("username"));
                    user.setOauthGoogleEmail(resultSet.getString("oauth_google_email"));
                    return user;
                }
            }
        );
    }

    public List<User> getStudentsByClassId(long classId) {
        return jdbcTemplate.query(
            "Se"
        )
    }
}

