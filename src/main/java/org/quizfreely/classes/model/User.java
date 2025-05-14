package org.quizfreely.classes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String displayName;
    private String oauthGoogleEmail;

    public User() {
        displayName = "User";
    }
    public User(UUID id) {
        this.id = id;
    }
    public User(UUID id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }
}

