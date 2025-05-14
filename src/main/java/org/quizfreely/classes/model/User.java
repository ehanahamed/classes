package org.quizfreely.classes.model;

import org.quizfreely.classes.AuthType;

public class User {
    private UUID id;
    private String displayName;
    private AuthType authType;
    private String username;
    private String oauthGoogleEmail;

    public User(
        UUID id,
        String displayName,
        AuthType authType,
        String usernameOrEmail
    ) {
        this.id = id;
        this.displayName = displayName;
        this.authType = authType;
        if (authType == AuthType.USERNAME_PASSWORD) {
            this.username = usernameOrEmail;
        } else {
            this.oauthGoogleEmail = usernameOrEmail;
        }
    }
}
