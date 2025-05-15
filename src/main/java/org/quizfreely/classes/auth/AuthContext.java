package org.quizfreely.classes.auth;

public class AuthContext {
    private boolean authed = false;
    private AuthedUser authedUser;
    
    public AuthContext(boolean authed) {
        this.authed = authed;
    }

    public AuthContext(
        boolean authed,
        UUID id,
        String displayName,
        AuthType authType,
        String username,
        String oauthGoogleEmail
    ) {
        this.authed = authed;
        this.authedUser = new AuthedUser();
        this.authedUser.setId(id);
        this.authedUser.setDisplayName(displayName);
        this.authedUser.setAuthType(authType);
        this.authedUser.setUsername(username);
        this.authedUser.setOauthGoogleEmail(oauthGoogleEmail);
    }

    public boolean isAuthed() {
        return authed;
    }
    public AuthedUser getAuthedUser() {
        return authedUser;
    }
}

