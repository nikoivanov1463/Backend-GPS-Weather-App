package com.tracking.tracking_app.Repositories;

import com.tracking.tracking_app.Entities.ResetPasswordToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends MongoRepository<ResetPasswordToken, String> {
    Optional<ResetPasswordToken> findByToken(String token);
}
