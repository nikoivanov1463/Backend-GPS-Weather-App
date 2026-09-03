package com.tracking.tracking_app.Services;

import com.tracking.tracking_app.DTOs.MarkerRequestDTO;
import com.tracking.tracking_app.Entities.Marker;
import com.tracking.tracking_app.Entities.User;
import com.tracking.tracking_app.Repositories.MarkerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

@Service
public class MarkerService {
    private final MarkerRepository markerRepository;

    public MarkerService(MarkerRepository markerRepository){
        this.markerRepository = markerRepository;
    }

    public void saveMarker(MarkerRequestDTO markerRequest){
        Marker marker = new Marker();
        marker.setUserId(getAuthenticatedUser().getId().toHexString());
        marker.setMarkerDate(markerRequest.getMarkerDate());
        marker.setLatitude(markerRequest.getLatitude());
        marker.setLongitude(markerRequest.getLongitude());
        marker.setColor(markerRequest.getColor());
        marker.setTitle(markerRequest.getTitle());

        if ("blue".equals(markerRequest.getColor())) {
            marker.setType("In Progress");
        } else {
            marker.setType("Finished");
        }

        markerRepository.save(marker);
    }

    public List<Map<String, Serializable>> fetchMarkers(){
        Optional<List<Marker>> result = markerRepository.findAllByUserId(getAuthenticatedUser().getId().toHexString());

        if(result.isPresent()){
            List<Map<String, Serializable>> coordinates = new ArrayList<>();

            for(Marker item : result.get()){
                coordinates.add(Map.of(
                        "markerID", item.getMarkerDate(),
                        "lat", item.getLatitude(),
                        "lng", item.getLongitude(),
                        "type", item.getType(),
                        "title", item.getTitle()
                ));
            }

            return coordinates;
        }

        return Collections.emptyList();
    }

    public void removeMarker(MarkerRequestDTO markerRequestDTO){
        markerRepository.deleteByMarkerDate(markerRequestDTO.getMarkerDate());
    }

    private User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (User) authentication.getPrincipal();
    }
}
