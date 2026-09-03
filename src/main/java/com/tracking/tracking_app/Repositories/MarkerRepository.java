package com.tracking.tracking_app.Repositories;

import com.tracking.tracking_app.Entities.Marker;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface MarkerRepository extends MongoRepository<Marker, String> {
    @Override
    void deleteById(String id);

    Optional<List<Marker>> findAllByUserId(String userId);

    void deleteByMarkerDate(String markerDate);
}
