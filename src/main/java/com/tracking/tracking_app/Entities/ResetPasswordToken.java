package com.tracking.tracking_app.Entities;

import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;

@Document(value = "reset_tokens")
public class ResetPasswordToken {
    private static final int EXPIRATION_TIME = 60 * 20;

    @Id
    private ObjectId id;

    private final String token;

    @DocumentReference(collection = "users")
    @NotNull
    private final User user;

    private Instant expiryDate;

    public ResetPasswordToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expiryDate = Instant.now().plusSeconds(EXPIRATION_TIME);
    }

    public ObjectId getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }


    public User getUser() {
        return user;
    }
}
