package org.quizfreely.classes.models;

import java.util.UUID;
import org.quizfreely.classes.auth.AuthType;

public class User {
    private UUID id;
    private String displayName;
    private String username;
    private String oauthGoogleEmail;

    public User() {}

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

