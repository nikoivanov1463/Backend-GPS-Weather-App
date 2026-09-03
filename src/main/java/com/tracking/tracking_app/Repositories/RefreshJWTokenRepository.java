package com.tracking.tracking_app.Repositories;

import com.tracking.tracking_app.Entities.RefreshJWToken;
import com.tracking.tracking_app.Entities.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshJWTokenRepository extends MongoRepository<RefreshJWToken, String> {
    void deleteByUserId(String userId);

    boolean existsByUserId(String userId);
}
