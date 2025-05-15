package org.quizfreely.classes.auth;

import java.util.UUID;

public class AuthedUser {
    private UUID id;
    private String displayName;
    private AuthType authType;
    private String username;
    private String oauthGoogleEmail;

    public AuthedUser() {}

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public AuthType getAuthType() {
        return authType;
    }
    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getOauthGoogleEmail() {
        return oauthGoogleEmail;
    }
    public void setOauthGoogleEmail(String email) {
        oauthGoogleEmail = email;
    }
}

