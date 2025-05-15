package org.quizfreely.classes.auth;

public class AuthRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public AuthContext authContextUsingToken(String authToken) {
        if (authToken) {
            jdbcTemplate.queryForObject(
                "SELECT u.id, u.display_name, u.auth_type, u.oauth_google_email FROM auth.users u " +
                "JOIN auth.sessions s ON u.id = s.user_id " +
                "WHERE s.token = ?",
                new Object[] { authToken ],
                (resultSet) -> {
                    if (resultSet.first()) {
                        return new AuthContext(
                            true,
                            resultSet.getLong("id"),
                            resultSet.getString("display_name"),
                            AuthType.valueOf(
                                resultSet.getString("auth_type").toUpperCase()
                            ),
                            resultSet.getString("username");
                            resultSet.getString("oauth_google_email");
                        );
                    } else {
                        return new AuthContext(false);
                    }
                }
            )
        } else {
            return new AuthContext(false);
        } 
    }
}

