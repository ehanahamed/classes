package org.quizfreely.classes.repos;

import org.springframework.jdbc.core.JdbcTemplate;
import org.quizfreely.classes.models.User;

public class UserRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User getUserById(UUID id) {
        jdbcTemplate.queryForObject(
            "SELECT id, display_name, username, oauth_google_email " +
            "FROM public.profiles WHERE id = ?",
            new Object[] { id },
            (resultSet) -> {
                User user = new User();
                user.setId(resultSet.getUUID("id"));
                user.setDisplayName(resultSet.getString("display_name"));
                user.setUsername(resultSet.getString("username"));
                user.setOauthGoogleEmail(resultSet.getString("oauth_google_email"));
            }
        )
    }
}

