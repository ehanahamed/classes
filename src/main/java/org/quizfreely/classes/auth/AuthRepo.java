package org.quizfreely.classes.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public class AuthRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public AuthContext authContextUsingToken(String authToken) {
        if (authToken != null && !authToken.isEmpty()) {
            try {
                return jdbcTemplate.queryForObject(
                    "SELECT u.id, u.display_name, u.auth_type, u.username, u.oauth_google_email " +
                    "FROM auth.users u JOIN auth.sessions s ON u.id = s.user_id " +
                    "WHERE s.token = ?",
                    new Object[] { authToken },
                    (resultSet, rowNum) -> new AuthContext(
                        true,
                        (UUID) resultSet.getObject("id"),
                        resultSet.getString("display_name"),
                        AuthType.valueOf(
                            resultSet.getString("auth_type").toUpperCase()
                        ),
                        resultSet.getString("username"),
                        resultSet.getString("oauth_google_email")
                    )
                );
            } catch (EmptyResultDataAccessException e) {
                return new AuthContext(false);
            }
        } else {
            return new AuthContext(false);
        } 
    }
}

